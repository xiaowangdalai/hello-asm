import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;


public class AddFieldAdapter extends ClassAdapter {
	private int fAcc;
	private String mName;
	private String mDesc;
	private boolean isFieldPresent;

	public AddFieldAdapter(ClassVisitor cv, int acc, String name, String desc) {
		super(cv);
		
		fAcc = acc;
		mName = name;
		mDesc = desc;
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, 
			String signature, Object value) {
		if (name.equals(desc)) {
			isFieldPresent = true;
		}
		
		return super.visitField(access, name, desc, signature, value);
	}

	@Override
	public void visitEnd() {
		if (!isFieldPresent) {
			FieldVisitor fv = cv.visitField(fAcc, mName, mDesc, null, null);
			
			if (fv != null) {
				fv.visitEnd();
			}
		}
		
		super.visitEnd();
	}

}
