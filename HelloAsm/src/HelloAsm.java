import java.io.IOException;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class HelloAsm {

	public static void main(String[] args) {
		ClassPrinter cp = new ClassPrinter();
		
		try {
			ClassReader cr = new ClassReader("java.lang.Runnable");
			cr.accept(cp, 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("**************************************************");
		
		ClassWriter cw = new ClassWriter(0);
		cw.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE, 
				"Comparable", null, 
				"java/lang/Object", new String[] { "Mesurable"});
		
		cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_FINAL, 
				"LESS", "I", null, new Integer(-1)).visitEnd();
		cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_FINAL, "EQUAL", "I", 
				null, new Integer(0)).visitEnd();
		cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_FINAL, "GREATER", "I", 
				null, new Integer(1)).visitEnd();
		cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I",
				null, null).visitEnd();;// 权限，方法名，描述，参数对应泛型，异常（数组）
		
		cw.visitEnd();
		byte[] b = cw.toByteArray();
		
		MyclassLoader myClassLoader = new MyclassLoader();
		Class<?> c = myClassLoader.defineClass("Comparable", b);
		
		ClassReader cr1 = new ClassReader(b);
		cr1.accept(cp, 0);
		
		System.out.println("**************************************************");
		
		ClassWriter cw1 = new ClassWriter(0);
		ClassAdapter ca1 = new changeVersionAdapter(cw1);
		ClassReader cr2 = new ClassReader(b);
		cr2.accept(ca1, 0);
		
		byte[] b1 = cw1.toByteArray();
		ClassReader cr3 = new ClassReader(b1);
		cr3.accept(cp, 0);
		
		System.out.println("**************************************************");
		
		ClassWriter cw2 = new ClassWriter(0);
		ClassAdapter ca2 = new RemoveMethodAdapter(cw2, "compareTo", "(Ljava/lang/Object;)I");
		ClassReader cr4 = new ClassReader(b1);
		cr4.accept(ca2, 0);
		
		byte[] b2 = cw2.toByteArray();
		ClassReader cr5 = new ClassReader(b2);
		cr5.accept(cp, 0);
		
		System.out.println("**************************************************");
		
		ClassWriter cw3 = new ClassWriter(0);
		ClassAdapter ca3 = new AddFieldAdapter(cw3, Opcodes.ACC_PUBLIC, "mAddedField", "I");
		ClassReader cr6 = new ClassReader(b2);
		cr6.accept(ca3, 0);
		
		byte[] b3 = cw3.toByteArray();
		ClassReader cr7 = new ClassReader(b3);
		cr7.accept(cp, 0);
		
		System.out.println("**************************************************");
	}
	
	static class changeVersionAdapter extends ClassAdapter {
		

		public changeVersionAdapter(ClassVisitor cv) {
			super(cv);
		}

		@Override
		public void visit(int version, int access, String name, String signature, String superName, 
				String[] interfaces) {
			cv.visit(Opcodes.V1_5, access, name, signature, superName, interfaces);
		}
		
	}
	
	static class MyclassLoader extends ClassLoader {
		public Class<?> defineClass(String name, byte[] b) {
			return defineClass(name, b, 0, b.length);
		}
	}
	
	static class ClassPrinter implements ClassVisitor {

		@Override
		public void visit(int version, int access, String name, String signature, String superName, 
				String[] interfaces) {
			System.out.println("version " + version + "\n");
			System.out.println(name + " extends " + superName + " {");
		}

		@Override
		public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
			return null;
		}

		@Override
		public void visitAttribute(Attribute arg0) {
		}

		@Override
		public void visitEnd() {
			System.out.println("}");
		}

		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, 
				Object value) {
			System.out.println("" + desc + "" + name); 
			return null;
		}

		@Override
		public void visitInnerClass(String arg0, String arg1, String arg2, int arg3) {
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature,
				String[] exceptions) {
			System.out.println(name + desc);
			return null;
		}

		@Override
		public void visitOuterClass(String arg0, String arg1, String arg2) {
		}

		@Override
		public void visitSource(String arg0, String arg1) {
		}
		
	}

}
