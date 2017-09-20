/**
 * CameraSettingsFragment.java
 * Yuneec-SDK-Android-Example
 * <p>
 * Copyright @ 2016-2017 Yuneec.
 * All rights reserved.
 */
package com.yuneec.example.component.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.yuneec.example.R;
import com.yuneec.example.component.listeners.CameraModeListener;
import com.yuneec.example.component.listeners.CameraSettingsListener;
import com.yuneec.example.component.utils.Common;
import com.yuneec.example.component.utils.Media;
import com.yuneec.sdk.Camera;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CameraSettingsFragment extends Fragment implements View.OnClickListener,
    AdapterView.OnItemSelectedListener {

    View rootView;

    Spinner wb_spinner;

    Spinner color_mode_spinner;

    Spinner exposure_spinner;

    Spinner ex_com_spinner;

    Spinner shutter_speed_spinner;

    Spinner iso_spinner;

    TextView ex_com_view;

    TextView iso_view;

    TextView shutter_speed_view;

    Camera.ShutterSpeedS shutterSpeedS = new Camera.ShutterSpeedS();

    Button capturePicture;

    Button video;

    private Camera.Mode cameraMode = Camera.Mode.UNKNOWN;

    private static final String TAG = CameraSettingsListener.class.getCanonicalName();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initViews(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();
        CameraModeListener.registerCameraModeListener(getActivity());
        CameraSettingsListener.registerSettingsListener();
        addOnClickListeners();
    }

    @Override
    public void onStop() {

        super.onStop();
        CameraModeListener.unRegisterCameraModeListener();
        CameraSettingsListener.unRegisterCameraSettingsListeners();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("wbSpinner", wb_spinner.getSelectedItemPosition());
        outState.putInt("colorModeSpinner", color_mode_spinner.getSelectedItemPosition());
        outState.putInt("exposureSpinner", exposure_spinner.getSelectedItemPosition());
        outState.putInt("exComSpinner", ex_com_spinner.getSelectedItemPosition());
        if (iso_spinner.getVisibility() == View.VISIBLE) {
            outState.putInt("isoSpinner", iso_spinner.getSelectedItemPosition());
        }
        if (shutter_speed_spinner.getVisibility() == View.VISIBLE) {
            outState.putInt("shutterSpeedSpinner", shutter_speed_spinner.getSelectedItemPosition());
        }
    }

    private void initViews(LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.camera_setting_layout, container, false);
        wb_spinner = (Spinner) rootView.findViewById(R.id.wb_dropdown);
        color_mode_spinner = (Spinner) rootView.findViewById(R.id.color_mode_dropdown);
        exposure_spinner = (Spinner) rootView.findViewById(R.id.exposure_mode_dropdown);
        ex_com_spinner = (Spinner) rootView.findViewById(R.id.exposure_com_dropdown);
        shutter_speed_spinner = (Spinner) rootView.findViewById(R.id.shutter_speed_dropdown);
        iso_spinner = (Spinner) rootView.findViewById(R.id.iso_dropdown);
        ex_com_view = (TextView) rootView.findViewById(R.id.ex_com_text);
        iso_view = (TextView) rootView.findViewById(R.id.iso_val);
        shutter_speed_view = (TextView) rootView.findViewById(R.id.shutter_speed);
        if (savedInstanceState != null) {
            wb_spinner.setSelection(savedInstanceState.getInt("wbSpinner"));
            color_mode_spinner.setSelection(savedInstanceState.getInt("colorModeSpinner"));
            exposure_spinner.setSelection(savedInstanceState.getInt("exposureSpinner"));
            ex_com_spinner.setSelection(savedInstanceState.getInt("exComSpinner"));
            if (iso_spinner.getVisibility() == View.VISIBLE) {
                iso_spinner.setSelection(savedInstanceState.getInt("isoSpinner"));
            }
            if (shutter_speed_spinner.getVisibility() == View.VISIBLE) {
                shutter_speed_spinner.setSelection(savedInstanceState.getInt("shutterSpeedSpinner"));
            }
        }
        capturePicture = (Button) rootView.findViewById(R.id.capturePicture);
        video = (Button) rootView.findViewById(R.id.video);
    }

    private void addOnClickListeners() {
        addItemsOnSpinner();
        /* work around for https://stackoverflow.com/questions/2562248/how-to-keep-onitemselected-from-firing-off-on-a-newly-instantiated-spinner*/
        setSelection();
        wb_spinner.setOnItemSelectedListener(this);
        color_mode_spinner.setOnItemSelectedListener(this);
        exposure_spinner.setOnItemSelectedListener(this);
        ex_com_spinner.setOnItemSelectedListener(this);
        shutter_speed_spinner.setOnItemSelectedListener(this);
        iso_spinner.setOnItemSelectedListener(this);
        capturePicture.setOnClickListener(this);
        video.setOnClickListener(this);
    }

    private void setSelection() {
        wb_spinner.setSelection(0, false);
        color_mode_spinner.setSelection(0, false);
        exposure_spinner.setSelection(0, false);
        ex_com_spinner.setSelection(0, false);
        shutter_speed_spinner.setSelection(0, false);
        iso_spinner.setSelection(0, false);
    }

    public void addItemsOnSpinner() {
        List<Camera.WhiteBalance> wbList =
            new ArrayList<>(EnumSet.allOf(Camera.WhiteBalance.class));
        ArrayAdapter<Camera.WhiteBalance> wbAdapter = new ArrayAdapter<>(getActivity(),
                                                                         R.layout.support_simple_spinner_dropdown_item, wbList);
        wbAdapter.setDropDownViewResource(R.layout.spinner_item);
        wb_spinner.setAdapter(wbAdapter);

        List<Camera.ColorMode> colorModeList =
            new ArrayList<>(EnumSet.allOf(Camera.ColorMode.class));
        ArrayAdapter<Camera.ColorMode> colorModeAdapter = new ArrayAdapter<>(getActivity(),
                                                                             R.layout.support_simple_spinner_dropdown_item, colorModeList);
        colorModeAdapter.setDropDownViewResource(R.layout.spinner_item);
        color_mode_spinner.setAdapter(colorModeAdapter);

        List<Camera.ExposureMode> exposureModeList =
            new ArrayList<>(EnumSet.allOf(Camera.ExposureMode.class));
        ArrayAdapter<Camera.ExposureMode> exposureModeAdapter = new ArrayAdapter<>(getActivity(),
                                                                                   R.layout.support_simple_spinner_dropdown_item, exposureModeList);
        exposureModeAdapter.setDropDownViewResource(R.layout.spinner_item);
        exposure_spinner.setAdapter(exposureModeAdapter);

        List<Float> exposureComList = new ArrayList<>();
        exposureComList.add(-2.0f);
        exposureComList.add(-1.5f);
        exposureComList.add(-1.0f);
        exposureComList.add(-0.5f);
        exposureComList.add(0.0f);
        exposureComList.add(0.5f);
        exposureComList.add(1.0f);
        exposureComList.add(1.5f);
        exposureComList.add(2.0f);

        ArrayAdapter<Float> exposureComAdapter = new ArrayAdapter<>(getActivity(),
                                                                    R.layout.support_simple_spinner_dropdown_item, exposureComList);
        exposureModeAdapter.setDropDownViewResource(R.layout.spinner_item);
        ex_com_spinner.setAdapter(exposureComAdapter);

        List<Integer> isoValLIst = new ArrayList<>();

        isoValLIst.add(100);
        isoValLIst.add(150);
        isoValLIst.add(200);
        isoValLIst.add(300);
        isoValLIst.add(400);
        isoValLIst.add(600);
        isoValLIst.add(800);
        isoValLIst.add(1600);
        isoValLIst.add(3200);

        ArrayAdapter<Integer> isoAdapter = new ArrayAdapter<>(getActivity(),
                                                              R.layout.support_simple_spinner_dropdown_item, isoValLIst);
        isoAdapter.setDropDownViewResource(R.layout.spinner_item);
        iso_spinner.setAdapter(isoAdapter);


        List<String> shutterSpeedList = new ArrayList<>();
        shutterSpeedList.add("4");
        shutterSpeedList.add("3");
        shutterSpeedList.add("2");
        shutterSpeedList.add("1");
        shutterSpeedList.add("1/30");
        shutterSpeedList.add("1/60");
        shutterSpeedList.add("1/125");
        shutterSpeedList.add("1/250");
        shutterSpeedList.add("1/500");
        shutterSpeedList.add("1/1000");
        shutterSpeedList.add("1/2000");
        shutterSpeedList.add("1/4000");
        shutterSpeedList.add("1/8000");



        ArrayAdapter<String> shutterSpeedAdapter = new ArrayAdapter<>(getActivity(),
                                                                      R.layout.support_simple_spinner_dropdown_item, shutterSpeedList);
        shutterSpeedAdapter.setDropDownViewResource(R.layout.spinner_item);
        shutter_speed_spinner.setAdapter(shutterSpeedAdapter);

    }


    @Override
    public void onClick(View v) {
        if (Common.isConnected) {
            switch (v.getId()) {
                case R.id.capturePicture:
                    Media.vibrate(getActivity());
                    if (!cameraMode.equals(Camera.Mode.PHOTO)) {
                        Camera.setMode(Camera.Mode.PHOTO, CameraModeListener.getCameraModeListener());
                        cameraMode = Camera.Mode.PHOTO;
                    } else {
                        Camera.asyncTakePhoto();
                    }
                    break;
                case R.id.video:
                    Media.vibrate(getActivity());
                    if (!cameraMode.equals(Camera.Mode.VIDEO)) {
                        Camera.setMode(Camera.Mode.VIDEO, CameraModeListener.getCameraModeListener());
                        cameraMode = Camera.Mode.VIDEO;
                    } else {
                        if (video.getText().equals("Start Video")) {
                            Camera.asyncStartVideo();
                            video.setText("Stop Video");
                        } else {
                            Camera.asyncStopVideo();
                            video.setText("Start Video");
                        }
                    }
            }
        } else {
            Common.makeToast(getActivity(), "Please Connect To The Drone");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.wb_dropdown:
                Log.d(TAG, "wb selected");
                Camera.WhiteBalance wbSelection = (Camera.WhiteBalance) wb_spinner.getSelectedItem();
                Camera.setWhiteBalance(wbSelection, CameraSettingsListener.getWhiteBalanceListener());
                break;
            case R.id.color_mode_dropdown:
                Log.d(TAG, "color mode selected");
                Camera.ColorMode colorModeSelection = (Camera.ColorMode) color_mode_spinner.getSelectedItem();
                Camera.setColorMode(colorModeSelection, CameraSettingsListener.getColorModeListener());
                break;
            case R.id.exposure_mode_dropdown:
                Log.d(TAG, "em selected");
                Camera.ExposureMode exposureModeSelection = (Camera.ExposureMode)
                                                            exposure_spinner.getSelectedItem();
                if (exposureModeSelection == Camera.ExposureMode.AUTO
                        || exposureModeSelection == Camera.ExposureMode.UNKNOWN) {
                    ex_com_spinner.setVisibility(View.VISIBLE);
                    ex_com_view.setVisibility(View.VISIBLE);
                    iso_view.setVisibility(View.GONE);
                    iso_spinner.setVisibility(View.GONE);
                    shutter_speed_spinner.setVisibility(View.GONE);
                    shutter_speed_view.setVisibility(View.GONE);
                    Camera.setExposureMode(exposureModeSelection, CameraSettingsListener.getExposureModeListener());
                } else {
                    ex_com_spinner.setVisibility(View.GONE);
                    ex_com_view.setVisibility(View.GONE);
                    iso_spinner.setVisibility(View.VISIBLE);
                    iso_view.setVisibility(View.VISIBLE);
                    shutter_speed_spinner.setVisibility(View.VISIBLE);
                    shutter_speed_view.setVisibility(View.VISIBLE);
                    Camera.setExposureMode(exposureModeSelection, CameraSettingsListener.getExposureModeListener());
                }
                break;
            case R.id.exposure_com_dropdown:
                Log.d(TAG, "eval selected");
                Float exposure_com_selection = (Float) ex_com_spinner.getSelectedItem();
                Camera.setExposureValue(exposure_com_selection, CameraSettingsListener.getExposureValueListener());
                break;
            case R.id.iso_dropdown:
                Log.d(TAG, "iso selected");
                int iso_selection = (int) iso_spinner.getSelectedItem();
                Camera.setISOValue(iso_selection, CameraSettingsListener.getIsoValueListener());
                break;
            case R.id.shutter_speed_dropdown:
                Log.d(TAG, "shutter speed selected");
                switch (shutter_speed_spinner.getSelectedItemPosition() + 1) {
                    case 1:
                        shutterSpeedS.numerator = 4;
                        shutterSpeedS.denominator = 1;
                        Log.d(TAG, shutterSpeedS.denominator + "");
                        Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                        break;
                    case 2:
                        shutterSpeedS.numerator = 3;
                        shutterSpeedS.denominator = 1;
                        Log.d(TAG, shutterSpeedS.denominator + "");
                        Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                        break;
                    case 3:
                        shutterSpeedS.numerator = 2;
                        shutterSpeedS.denominator = 1;
                        Log.d(TAG, shutterSpeedS.denominator + "");
                        Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                        break;
                    case 4:
                        shutterSpeedS.numerator = 1;
                        shutterSpeedS.denominator = 1;
                        Log.d(TAG, shutterSpeedS.denominator + "");
                        Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                        break;
                    case 5:
                        shutterSpeedS.numerator = 1;
                        shutterSpeedS.denominator = 30;
                        Log.d(TAG, shutterSpeedS.denominator + "");
                        Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                        break;
                    case 6:
                        shutterSpeedS.numerator = 1;
                        shutterSpeedS.denominator = 60;
                        Log.d(TAG, shutterSpeedS.denominator + "");
                        Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                        break;
                    case 7:
                        shutterSpeedS.numerator = 1;
                        shutterSpeedS.denominator = 125;
                        Log.d(TAG, shutterSpeedS.denominator + "");
                        Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                        break;
                    case 8:
                        shutterSpeedS.numerator = 1;
                        shutterSpeedS.denominator = 250;
                        Log.d(TAG, shutterSpeedS.denominator + "");
                        Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                        break;
                    case 9:
                        shutterSpeedS.numerator = 1;
                        shutterSpeedS.denominator = 500;
                        Log.d(TAG, shutterSpeedS.denominator + "");
                        Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                        break;
                    case 10:
                        shutterSpeedS.numerator = 1;
                        shutterSpeedS.denominator = 1000;
                        Log.d(TAG, shutterSpeedS.denominator + "");
                        Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                        break;
                    case 11:
                        shutterSpeedS.numerator = 1;
                        shutterSpeedS.denominator = 2000;
                        Log.d(TAG, shutterSpeedS.denominator + "");
                        Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                        break;
                    case 12:
                        shutterSpeedS.numerator = 1;
                        shutterSpeedS.denominator = 4000;
                        Log.d(TAG, shutterSpeedS.denominator + "");
                        Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                        break;
                    case 13:
                        shutterSpeedS.numerator = 1;
                        shutterSpeedS.denominator = 8000;
                        Log.d(TAG, shutterSpeedS.denominator + "");
                        Camera.setShutterSpeed(shutterSpeedS, CameraSettingsListener.getShutterSpeedListener());
                        break;



                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}