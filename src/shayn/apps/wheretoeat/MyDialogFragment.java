package shayn.apps.wheretoeat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {
	
	final static String TAG = "AddUserDialogFragment";
	
	public static MyDialogFragment newInstance(int title, String positive, String negative) {
		MyDialogFragment frag = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putString("positive", positive);
        args.putString("negative", negative);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        String posStr = getArguments().getString("positive");
        String negStr = getArguments().getString("negative");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(posStr,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }
                )
                .setNegativeButton(negStr,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    }
                )
                .create();
    }
}
