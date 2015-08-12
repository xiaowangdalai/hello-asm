import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;


public class RemoveMethodAdapter extends ClassAdapter {
	private String mName;
	private String mDesc;

	public RemoveMethodAdapter(ClassVisitor cv, String name, String desc) {
		super(cv);
		
		mName = name;
		mDesc = desc;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, 
			String signature, String[] exceptions) {
		if (name.equals(mName) && desc.equals(mDesc)) {
			return null;
		}
		
		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	
}
