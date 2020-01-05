package com.romanport.deltawebmap.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Dino.DinoResponseData;
import com.romanport.deltawebmap.Framework.API.Entities.ArkDinoStatIndexes;
import com.romanport.deltawebmap.Framework.ImageTool;
import com.romanport.deltawebmap.Framework.Session.Actions.DownloadDinoDataAction;
import com.romanport.deltawebmap.Framework.Session.DeltaServerCallback;
import com.romanport.deltawebmap.R;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     DinoDataDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link DinoDataDialogListener}.</p>
 */
public class DinoDataDialogFragment extends BottomSheetDialogFragment {

    private static final String ARG_DINO_ID = "dino_id";
    private static final String ARG_ICON_URL = "icon_url";
    private static final String ARG_TITLE = "title";
    private static final String ARG_SUBTITLE = "subtitle";
    private DinoDataDialogListener mListener;

    // TODO: Customize parameters
    public static DinoDataDialogFragment newInstance(String dinoId, String iconUrl, String title, String subtitle) {
        final DinoDataDialogFragment fragment = new DinoDataDialogFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_DINO_ID, dinoId);
        args.putString(ARG_ICON_URL, iconUrl);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_SUBTITLE, subtitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dino_data_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Get data
        String dinoId = getArguments().getString(ARG_DINO_ID);
        String iconUrl = getArguments().getString(ARG_ICON_URL);
        String title = getArguments().getString(ARG_TITLE);
        String subtitle = getArguments().getString(ARG_SUBTITLE);

        //Hide detailed content until we get it
        view.findViewById(R.id.dinoDataBottom).setVisibility(View.INVISIBLE);

        //Apply data
        ImageTool.SetImageOnView(iconUrl, (ImageView)view.findViewById(R.id.dinoDataIconView));
        ((TextView)view.findViewById(R.id.dinoDataTitle)).setText(title);
        ((TextView)view.findViewById(R.id.dinoDataSubtitle)).setText(subtitle);

        //Create action
        DownloadDinoDataAction action = new DownloadDinoDataAction();
        action.id = dinoId;
        action.fragment = this;
        mListener.QueueAction(action);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (DinoDataDialogListener) parent;
        } else {
            mListener = (DinoDataDialogListener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public void OnFullDataDownloaded(DinoResponseData data) {
        View v = getView();

        //Set status bars
        SetStatusBar(v.findViewById(R.id.dinoDataStatHealth), R.drawable.arkstatusicon_health, R.string.ark_status_health, Math.round(data.dino.max_stats[ArkDinoStatIndexes.HEALTH]), Math.round(data.dino.current_stats[ArkDinoStatIndexes.HEALTH]));
        SetStatusBar(v.findViewById(R.id.dinoDataStatStamina), R.drawable.arkstatusicon_stamina, R.string.ark_status_stamina, Math.round(data.dino.max_stats[ArkDinoStatIndexes.STAMINA]), Math.round(data.dino.current_stats[ArkDinoStatIndexes.STAMINA]));
        SetStatusBar(v.findViewById(R.id.dinoDataStatWeight), R.drawable.arkstatusicon_inventoryweight, R.string.ark_status_weight, Math.round(data.dino.max_stats[ArkDinoStatIndexes.WEIGHT]), Math.round(data.dino.current_stats[ArkDinoStatIndexes.WEIGHT]));
        SetStatusBar(v.findViewById(R.id.dinoDataStatFood), R.drawable.arkstatusicon_food, R.string.ark_status_food, Math.round(data.dino.max_stats[ArkDinoStatIndexes.FOOD]), Math.round(data.dino.current_stats[ArkDinoStatIndexes.FOOD]));

        //Hide loading icon
        v.findViewById(R.id.dinoDataLoader).setVisibility(View.INVISIBLE);

        //Show content
        v.findViewById(R.id.dinoDataBottom).setVisibility(View.VISIBLE);
    }

    public void SetStatusBar(View statusBar, int iconResource, int nameResource, int max, int current) {
        ((ImageView) statusBar.findViewById(R.id.dino_stat_progressbar_image)).setImageResource(iconResource);
        ((TextView) statusBar.findViewById(R.id.dino_stat_progressbar_name)).setText(nameResource);
        ((TextView) statusBar.findViewById(R.id.dino_stat_progressbar_amount)).setText(current + "/" + max);
        ProgressBar b = (ProgressBar) statusBar.findViewById(R.id.dino_stat_progressbar);
        b.setMax(max);
        b.setProgress(current);
    }

    public interface DinoDataDialogListener {
        void QueueAction(DeltaServerCallback cb);
    }

}
