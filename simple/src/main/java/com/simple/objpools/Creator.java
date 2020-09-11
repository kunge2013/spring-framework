//package com.simple.objpools;
//
//import aj.org.objectweb.asm.AnnotationVisitor;
//import aj.org.objectweb.asm.ClassWriter;
//import aj.org.objectweb.asm.FieldVisitor;
//import aj.org.objectweb.asm.MethodVisitor;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.*;
//import java.net.URL;
//import java.net.URLClassLoader;
//import java.net.URLConnection;
//import java.net.URLStreamHandler;
//import java.util.*;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.stream.Stream;
//
//import static aj.org.objectweb.asm.Opcodes.*;
//
///**
// * @author fangkun
// * @date 2020/9/9 19:14
// * @description:
// */
//public interface Creator<T> {
//
//
//	@SuppressWarnings("unchecked")
//	static class CreatorInner {
//
//		static final Logger logger = Logger.getLogger(Creator.class.getSimpleName());
//
//		static final Map<Class, Creator> creatorCacheMap = new HashMap<>();
//
//		static {
//			creatorCacheMap.put(Object.class, p -> new Object());
//			creatorCacheMap.put(ArrayList.class, p -> new ArrayList<>());
//			creatorCacheMap.put(HashMap.class, p -> new HashMap<>());
//			creatorCacheMap.put(HashSet.class, p -> new HashSet<>());
//			creatorCacheMap.put(Stream.class, p -> new ArrayList<>().stream());
//			creatorCacheMap.put(ConcurrentHashMap.class, p -> new ConcurrentHashMap<>());
//			creatorCacheMap.put(CompletableFuture.class, p -> new CompletableFuture<>());
//			creatorCacheMap.put(Map.Entry.class, new Creator<Map.Entry>() {
//				@Override
//				@ConstructorParameters({"key", "value"})
//				public Map.Entry create(Object... params) {
//					return new AbstractMap.SimpleEntry(params[0], params[1]);
//				}
//			});
//			creatorCacheMap.put(AbstractMap.SimpleEntry.class, new Creator<AbstractMap.SimpleEntry>() {
//				@Override
//				@ConstructorParameters({"key", "value"})
//				public AbstractMap.SimpleEntry create(Object... params) {
//					return new AbstractMap.SimpleEntry(params[0], params[1]);
//				}
//			});
//			creatorCacheMap.put(AnyValue.DefaultAnyValue.class, p -> new AnyValue.DefaultAnyValue());
//			creatorCacheMap.put(AnyValue.class, p -> new AnyValue.DefaultAnyValue());
//		}
//
//		static class SimpleClassVisitor extends ClassVisitor {
//
//			private final String constructorDesc;
//
//			private final List<String> fieldnames;
//
//			private boolean started;
//
//			public SimpleClassVisitor(int api, List<String> fieldnames, String constructorDesc) {
//				super(api);
//				this.fieldnames = fieldnames;
//				this.constructorDesc = constructorDesc;
//			}
//
//			@Override
//			public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//				if (java.lang.reflect.Modifier.isStatic(access) || !"<init>".equals(name)) return null;
//				if (constructorDesc != null && !constructorDesc.equals(desc)) return null;
//				if (this.started) return null;
//				this.started = true;
//				//返回的List中参数列表可能会比方法参数量多，因为方法内的临时变量也会存入list中， 所以需要list的元素集合比方法的参数多
//				return new MethodVisitor(Opcodes.ASM6) {
//					@Override
//					public void visitLocalVariable(String name, String description, String signature, Label start, Label end, int index) {
//						if (index < 1) return;
//						int size = fieldnames.size();
//						//index不会按顺序执行的
//						if (index > size) {
//							for (int i = size; i < index; i++) {
//								fieldnames.add(" ");
//							}
//							fieldnames.set(index - 1, name);
//						}
//						fieldnames.set(index - 1, name);
//					}
//				};
//			}
//		}
//
//		public static AbstractMap.SimpleEntry<String, Class>[] getConstructorField(Class clazz, int paramcount, String constructorDesc) {
//			String n = clazz.getName();
//			InputStream in = clazz.getResourceAsStream(n.substring(n.lastIndexOf('.') + 1) + ".class");
//			if (in == null) return null;
//			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
//			byte[] bytes = new byte[1024];
//			int pos;
//			try {
//				while ((pos = in.read(bytes)) != -1) {
//					out.write(bytes, 0, pos);
//				}
//				in.close();
//			} catch (IOException io) {
//				return null;
//			}
//			final List<String> fieldnames = new ArrayList<>();
//			new ClassReader(out.toByteArray()).accept(new SimpleClassVisitor(Opcodes.ASM6, fieldnames, constructorDesc), 0);
//			while (fieldnames.remove(" ")); //删掉空元素
//			if (fieldnames.isEmpty()) return null;
//			if (paramcount == fieldnames.size()) {
//				return getConstructorField(clazz, paramcount, fieldnames.toArray(new String[fieldnames.size()]));
//			} else {
//				String[] fs = new String[paramcount];
//				for (int i = 0; i < fs.length; i++) {
//					fs[i] = fieldnames.get(i);
//				}
//				return getConstructorField(clazz, paramcount, fs);
//			}
//		}
//
//		public static AbstractMap.SimpleEntry<String, Class>[] getConstructorField(Class clazz, int paramcount, String[] names) {
//			AbstractMap.SimpleEntry<String, Class>[] se = new AbstractMap.SimpleEntry[names.length];
//			for (int i = 0; i < names.length; i++) { //查询参数名对应的Field
//				try {
//					Field field = clazz.getDeclaredField(names[i]);
//					se[i] = new AbstractMap.SimpleEntry<>(field.getName(), field.getType());
//				} catch (NoSuchFieldException fe) {
//					Class cz = clazz;
//					Field field = null;
//					while ((cz = cz.getSuperclass()) != Object.class) {
//						try {
//							field = cz.getDeclaredField(names[i]);
//							break;
//						} catch (NoSuchFieldException nsfe) {
//						}
//					}
//					if (field == null) return null;
//					se[i] = new AbstractMap.SimpleEntry<>(field.getName(), field.getType());
//				} catch (Exception e) {
//					if (logger.isLoggable(Level.FINE)) logger.log(Level.FINE, clazz + " getConstructorField error", e);
//					return null;
//				}
//			}
//			return se;
//		}
//
//		public static AbstractMap.SimpleEntry<String, Class>[] getConstructorField(Class clazz, int paramcount, Parameter[] params) {
//			AbstractMap.SimpleEntry<String, Class>[] se = new AbstractMap.SimpleEntry[params.length];
//			for (int i = 0; i < params.length; i++) { //查询参数名对应的Field
//				try {
//					Field field = clazz.getDeclaredField(params[i].getName());
//					se[i] = new AbstractMap.SimpleEntry<>(field.getName(), field.getType());
//				} catch (Exception e) {
//					return null;
//				}
//			}
//			return se;
//		}
//	}
//
//	/**
//	 * 创建对象
//	 *
//	 * @param params 构造函数的参数
//	 *
//	 * @return 构建的对象
//	 */
//	public T create(Object... params);
//
//	/**
//	 * 根据指定的class采用ASM技术生产Creator。
//	 *
//	 * @param <T>   构建类的数据类型
//	 * @param clazz 构建类
//	 *
//	 * @return Creator对象
//	 */
//	@SuppressWarnings("unchecked")
//	public static <T> Creator<T> create(Class<T> clazz) {
//		if (List.class.isAssignableFrom(clazz) && (clazz.isAssignableFrom(ArrayList.class) || clazz.getName().startsWith("java.util.Collections") || clazz.getName().startsWith("java.util.ImmutableCollections") || clazz.getName().startsWith("java.util.Arrays"))) {
//			clazz = (Class<T>) ArrayList.class;
//		} else if (Map.class.isAssignableFrom(clazz) && (clazz.isAssignableFrom(HashMap.class) || clazz.getName().startsWith("java.util.Collections") || clazz.getName().startsWith("java.util.ImmutableCollections"))) {
//			clazz = (Class<T>) HashMap.class;
//		} else if (Set.class.isAssignableFrom(clazz) && (clazz.isAssignableFrom(HashSet.class) || clazz.getName().startsWith("java.util.Collections") || clazz.getName().startsWith("java.util.ImmutableCollections"))) {
//			clazz = (Class<T>) HashSet.class;
//		} else if (Map.class.isAssignableFrom(clazz) && clazz.isAssignableFrom(ConcurrentHashMap.class)) {
//			clazz = (Class<T>) ConcurrentHashMap.class;
//		} else if (Deque.class.isAssignableFrom(clazz) && (clazz.isAssignableFrom(ArrayDeque.class) || clazz.getName().startsWith("java.util.Collections") || clazz.getName().startsWith("java.util.ImmutableCollections"))) {
//			clazz = (Class<T>) ArrayDeque.class;
//		}  else if (Collection.class.isAssignableFrom(clazz) && clazz.isAssignableFrom(ArrayList.class)) {
//			clazz = (Class<T>) ArrayList.class;
//		} else if (Map.Entry.class.isAssignableFrom(clazz) && (Modifier.isInterface(clazz.getModifiers()) || Modifier.isAbstract(clazz.getModifiers()) || !Modifier.isPublic(clazz.getModifiers()))) {
//			clazz = (Class<T>) AbstractMap.SimpleEntry.class;
//		}
//		Creator creator = CreatorInner.creatorCacheMap.get(clazz);
//		if (creator != null) return creator;
//		if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
//			throw new RuntimeException("[" + clazz + "] is a interface or abstract class, cannot create it's Creator.");
//		}
//		for (final Method method : clazz.getDeclaredMethods()) { //查找类中是否存在提供创建Creator实例的静态方法
//			if (!Modifier.isStatic(method.getModifiers())) continue;
//			if (method.getParameterTypes().length != 0) continue;
//			if (method.getReturnType() != Creator.class) continue;
//			try {
//				method.setAccessible(true);
//				return (Creator<T>) method.invoke(null);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}
//		final String supDynName = Creator.class.getName().replace('.', '/');
//		final String interName = clazz.getName().replace('.', '/');
//		final String interDesc = Type.getDescriptor(clazz);
//		ClassLoader loader = Thread.currentThread().getContextClassLoader();
//		String newDynName = supDynName + "_" + clazz.getSimpleName() + "_" + (System.currentTimeMillis() % 10000);
//		if (String.class.getClassLoader() != clazz.getClassLoader()) {
//			loader = clazz.getClassLoader();
//			newDynName = interName + "_Dyn" + Creator.class.getSimpleName();
//		}
//		try {
//			return (Creator) loader.loadClass(newDynName.replace('/', '.')).getDeclaredConstructor().newInstance();
//		} catch (Throwable ex) {
//		}
//
//		Constructor<T> constructor0 = null;
//		AbstractMap.SimpleEntry<String, Class>[] constructorParameters0 = null; //构造函数的参数
//
//		if (constructor0 == null) {  // 1、查找public的空参数构造函数
//			for (Constructor c : clazz.getConstructors()) {
//				if (c.getParameterCount() == 0) {
//					constructor0 = c;
//					constructorParameters0 = new AbstractMap.SimpleEntry[0];
//					break;
//				}
//			}
//		}
//		if (constructor0 == null) {  // 2、查找public带ConstructorParameters注解的构造函数
//			for (Constructor c : clazz.getConstructors()) {
//				ConstructorParameters cp = (ConstructorParameters) c.getAnnotation(ConstructorParameters.class);
//				if (cp == null) continue;
//				AbstractMap.SimpleEntry<String, Class>[] fields = CreatorInner.getConstructorField(clazz, c.getParameterCount(), cp.value());
//				if (fields != null) {
//					constructor0 = c;
//					constructorParameters0 = fields;
//					break;
//				}
//			}
//		}
//		if (constructor0 == null) {  // 3、查找public且不带ConstructorParameters注解的构造函数
//			List<Constructor> cs = new ArrayList<>();
//			for (Constructor c : clazz.getConstructors()) {
//				if (c.getAnnotation(ConstructorParameters.class) != null) continue;
//				if (c.getParameterCount() < 1) continue;
//				cs.add(c);
//			}
//			//优先参数最多的构造函数
//			cs.sort((o1, o2) -> o2.getParameterCount() - o1.getParameterCount());
//			for (Constructor c : cs) {
//				AbstractMap.SimpleEntry<String, Class>[] fields = CreatorInner.getConstructorField(clazz, c.getParameterCount(), Type.getConstructorDescriptor(c));
//				if (fields != null) {
//					constructor0 = c;
//					constructorParameters0 = fields;
//					break;
//				}
//			}
//		}
//		if (constructor0 == null) {  // 4、查找非private带ConstructorParameters的构造函数
//			for (Constructor c : clazz.getDeclaredConstructors()) {
//				if (Modifier.isPublic(c.getModifiers()) || Modifier.isPrivate(c.getModifiers())) continue;
//				ConstructorParameters cp = (ConstructorParameters) c.getAnnotation(ConstructorParameters.class);
//				if (cp == null) continue;
//				AbstractMap.SimpleEntry<String, Class>[] fields = CreatorInner.getConstructorField(clazz, c.getParameterCount(), cp.value());
//				if (fields != null) {
//					constructor0 = c;
//					constructorParameters0 = fields;
//					break;
//				}
//			}
//		}
//		if (constructor0 == null) {  // 5、查找非private且不带ConstructorParameters的构造函数
//			List<Constructor> cs = new ArrayList<>();
//			for (Constructor c : clazz.getDeclaredConstructors()) {
//				if (Modifier.isPublic(c.getModifiers()) || Modifier.isPrivate(c.getModifiers())) continue;
//				if (c.getAnnotation(ConstructorParameters.class) != null) continue;
//				if (c.getParameterCount() < 1) continue;
//				cs.add(c);
//			}
//			//优先参数最多的构造函数
//			cs.sort((o1, o2) -> o2.getParameterCount() - o1.getParameterCount());
//			for (Constructor c : cs) {
//				AbstractMap.SimpleEntry<String, Class>[] fields = CreatorInner.getConstructorField(clazz, c.getParameterCount(), Type.getConstructorDescriptor(c));
//				if (fields != null) {
//					constructor0 = c;
//					constructorParameters0 = fields;
//					break;
//				}
//			}
//		}
//		final Constructor<T> constructor = constructor0;
//		final AbstractMap.SimpleEntry<String, Class>[] constructorParameters = constructorParameters0;
//		if (constructor == null || constructorParameters == null) {
//			throw new RuntimeException("[" + clazz + "] have no public or ConstructorParameters-Annotation constructor.");
//		}
//		//-------------------------------------------------------------
//		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//		FieldVisitor fv;
//		MethodVisitor mv;
//		AnnotationVisitor av0;
//		cw.visit(V1_8, ACC_PUBLIC + ACC_FINAL + ACC_SUPER, newDynName, "Ljava/lang/Object;L" + supDynName + "<" + interDesc + ">;", "java/lang/Object", new String[]{supDynName});
//
//		{//Creator自身的构造方法
//			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
//			mv.visitVarInsn(ALOAD, 0);
//			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
//			mv.visitInsn(RETURN);
//			mv.visitMaxs(1, 1);
//			mv.visitEnd();
//		}
//		{//create 方法
//			mv = cw.visitMethod(ACC_PUBLIC + ACC_VARARGS, "create", "([Ljava/lang/Object;)L" + interName + ";", null, null);
//			if (constructorParameters.length > 0) {
//				av0 = mv.visitAnnotation(Type.getDescriptor(ConstructorParameters.class), true);
//				AnnotationVisitor av1 = av0.visitArray("value");
//				for (AbstractMap.SimpleEntry<String, Class> n : constructorParameters) {
//					av1.visit(null, n.getKey());
//				}
//				av1.visitEnd();
//				av0.visitEnd();
//			}
//			final int[] iconsts = {ICONST_0, ICONST_1, ICONST_2, ICONST_3, ICONST_4, ICONST_5};
//			{  //有Primitive数据类型且值为null的参数需要赋默认值
//				for (int i = 0; i < constructorParameters.length; i++) {
//					final Class pt = constructorParameters[i].getValue();
//					if (!pt.isPrimitive()) continue;
//					mv.visitVarInsn(ALOAD, 1);
//					if (i < 6) {
//						mv.visitInsn(iconsts[i]);
//					} else if (i <= Byte.MAX_VALUE) {
//						mv.visitIntInsn(BIPUSH, i);
//					} else if (i <= Short.MAX_VALUE) {
//						mv.visitIntInsn(SIPUSH, i);
//					} else {
//						mv.visitLdcInsn(i);
//					}
//					mv.visitInsn(AALOAD);
//					Label lab = new Label();
//					mv.visitJumpInsn(IFNONNULL, lab);
//					mv.visitVarInsn(ALOAD, 1);
//					if (i < 6) {
//						mv.visitInsn(iconsts[i]);
//					} else if (i <= Byte.MAX_VALUE) {
//						mv.visitIntInsn(BIPUSH, i);
//					} else if (i <= Short.MAX_VALUE) {
//						mv.visitIntInsn(SIPUSH, i);
//					} else {
//						mv.visitLdcInsn(i);
//					}
//					if (pt == int.class) {
//						mv.visitInsn(ICONST_0);
//						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
//					} else if (pt == long.class) {
//						mv.visitInsn(LCONST_0);
//						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
//					} else if (pt == boolean.class) {
//						mv.visitFieldInsn(GETSTATIC, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
//					} else if (pt == short.class) {
//						mv.visitInsn(ICONST_0);
//						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
//					} else if (pt == float.class) {
//						mv.visitInsn(FCONST_0);
//						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
//					} else if (pt == byte.class) {
//						mv.visitInsn(ICONST_0);
//						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
//					} else if (pt == double.class) {
//						mv.visitInsn(DCONST_0);
//						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
//					} else if (pt == char.class) {
//						mv.visitInsn(ICONST_0);
//						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
//					}
//					mv.visitInsn(AASTORE);
//					mv.visitLabel(lab);
//				}
//			}
//			mv.visitTypeInsn(NEW, interName);
//			mv.visitInsn(DUP);
//			//---------------------------------------
//			{
//				for (int i = 0; i < constructorParameters.length; i++) {
//					mv.visitVarInsn(ALOAD, 1);
//					if (i < 6) {
//						mv.visitInsn(iconsts[i]);
//					} else if (i <= Byte.MAX_VALUE) {
//						mv.visitIntInsn(BIPUSH, i);
//					} else if (i <= Short.MAX_VALUE) {
//						mv.visitIntInsn(SIPUSH, i);
//					} else {
//						mv.visitLdcInsn(i);
//					}
//					mv.visitInsn(AALOAD);
//					final Class ct = constructorParameters[i].getValue();
//					if (ct.isPrimitive()) {
//						final Class bigct = Array.get(Array.newInstance(ct, 1), 0).getClass();
//						mv.visitTypeInsn(CHECKCAST, bigct.getName().replace('.', '/'));
//						try {
//							Method pm = bigct.getMethod(ct.getSimpleName() + "Value");
//							mv.visitMethodInsn(INVOKEVIRTUAL, bigct.getName().replace('.', '/'), pm.getName(), Type.getMethodDescriptor(pm), false);
//						} catch (Exception ex) {
//							throw new RuntimeException(ex); //不可能会发生
//						}
//					} else {
//						mv.visitTypeInsn(CHECKCAST, ct.getName().replace('.', '/'));
//					}
//				}
//			}
//			//---------------------------------------
//			mv.visitMethodInsn(INVOKESPECIAL, interName, "<init>", Type.getConstructorDescriptor(constructor), false);
//			mv.visitInsn(ARETURN);
//			mv.visitMaxs((constructorParameters.length > 0 ? (constructorParameters.length + 3) : 2), 2);
//			mv.visitEnd();
//		}
//		{ //虚拟 create 方法
//			mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_VARARGS + ACC_SYNTHETIC, "create", "([Ljava/lang/Object;)Ljava/lang/Object;", null, null);
//			mv.visitVarInsn(ALOAD, 0);
//			mv.visitVarInsn(ALOAD, 1);
//			mv.visitMethodInsn(INVOKEVIRTUAL, newDynName, "create", "([Ljava/lang/Object;)" + interDesc, false);
//			mv.visitInsn(ARETURN);
//			mv.visitMaxs(2, 2);
//			mv.visitEnd();
//		}
//		cw.visitEnd();
//		final byte[] bytes = cw.toByteArray();
//		final boolean ispub = Modifier.isPublic(constructor.getModifiers());
//		Class<?> resultClazz = null;
//		if (loader instanceof URLClassLoader && !ispub) {
//			try {
//				final URLClassLoader urlLoader = (URLClassLoader) loader;
//				final URL url = new URL("memclass", "localhost", -1, "/" + newDynName.replace('/', '.') + "/", new URLStreamHandler() {
//					@Override
//					protected URLConnection openConnection(URL u) throws IOException {
//						return new URLConnection(u) {
//							@Override
//							public void connect() throws IOException {
//							}
//
//							@Override
//							public InputStream getInputStream() throws IOException {
//								return new ByteArrayInputStream(bytes);
//							}
//						};
//					}
//				});
//				Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
//				addURLMethod.setAccessible(true);
//				addURLMethod.invoke(urlLoader, url);
//				resultClazz = urlLoader.loadClass(newDynName.replace('/', '.'));
//			} catch (Throwable t) { //异常无需理会， 使用下一种loader方式
//				t.printStackTrace();
//			}
//		}
//		if (!ispub && resultClazz == null) throw new RuntimeException("[" + clazz + "] have no public or ConstructorParameters-Annotation constructor.");
//		try {
//			if (resultClazz == null) resultClazz = new ClassLoader(loader) {
//				public final Class<?> loadClass(String name, byte[] b) {
//					return defineClass(name, b, 0, b.length);
//				}
//			}.loadClass(newDynName.replace('/', '.'), bytes);
//			return (Creator) resultClazz.getDeclaredConstructor().newInstance();
//		} catch (Exception ex) {
//			throw new RuntimeException(ex);
//		}
//	}
//}
