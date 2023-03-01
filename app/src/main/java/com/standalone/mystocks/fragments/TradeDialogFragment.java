package com.standalone.mystocks.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.standalone.mystocks.R;
import com.standalone.mystocks.constant.Config;
import com.standalone.mystocks.constant.ErrorMessages;
import com.standalone.mystocks.handlers.AssetTableHandler;
import com.standalone.mystocks.handlers.HistoryTableHandler;
import com.standalone.mystocks.handlers.generic.OpenDB;
import com.standalone.mystocks.interfaces.DialogCloseListener;
import com.standalone.mystocks.models.Stock;

import java.util.Objects;

public class TradeDialogFragment extends BottomSheetDialogFragment {
    public static final String TAG = TradeDialogFragment.class.getSimpleName();
    private EditText edSymbol;
    private EditText edShares;
    private EditText edPrice;

    private Stock referenceStock;
    private AssetTableHandler assetTableHandler;
    private HistoryTableHandler historyTableHandler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_trade, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize variables
        edSymbol = view.findViewById(R.id.edSymbol);
        edPrice = view.findViewById(R.id.edPrice);
        edShares = view.findViewById(R.id.edShares);
        Button btSubmit = view.findViewById(R.id.btSubmit);

        addCancelButton(edSymbol, R.id.imSymbol);
        addCancelButton(edPrice, R.id.imPrice);
        addCancelButton(edShares, R.id.imShares);

        // Fill fields if exists
        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            btSubmit.setText(Stock.OrderType.SELL.toString());
            btSubmit.setBackgroundResource(R.color.danger_dark);
            isUpdate = true;
            Stock s = (Stock) bundle.get("stock");
            assert s != null;
            referenceStock = s;
            edSymbol.setText(s.getSymbol());
            edPrice.setText(String.valueOf(s.getPrice()));
            edShares.setText(String.valueOf(s.getShares()));
        } else {
            btSubmit.setText(Stock.OrderType.BUY.toString());
            edSymbol.requestFocus();
        }

        OpenDB openDB = new OpenDB(getActivity(), Config.DATABASE_NAME, Config.VERSION);
        assetTableHandler = new AssetTableHandler(openDB);
        historyTableHandler = new HistoryTableHandler(openDB);

        openDB.init();

        final boolean finalIsUpdate = isUpdate;
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit(finalIsUpdate);
            }
        });
    }

    private void onSubmit(boolean isUpdate) {
        // Assert the fields are not empty
        if (edSymbol.getText().toString().equals("")) {
            edSymbol.setError(ErrorMessages.REQUIRED);
            return;
        }
        if (edPrice.getText().toString().equals("")) {
            edPrice.setError(ErrorMessages.REQUIRED);
            return;
        }
        if (edShares.getText().toString().equals("")) {
            edShares.setError(ErrorMessages.REQUIRED);
            return;
        }

        String inputSymbol = edSymbol.getText().toString().toUpperCase();

        double inputPrice;
        try {
            inputPrice = Double.parseDouble(edPrice.getText().toString());
        } catch (NumberFormatException e) {
            inputPrice = 0.0;
        }

        int inputShares;
        try {
            inputShares = Integer.parseInt(edShares.getText().toString());
        } catch (NumberFormatException e) {
            inputShares = 0;
        }

        if (isUpdate) {
            // Check if out of range
            Stock s = referenceStock;
            int remainingShares = s.getShares() - inputShares;
            double matchedPrice = s.getPrice();

            if (remainingShares < 0) {
                edShares.setError(ErrorMessages.INVALID);
                return;
            }

            s.setShares(inputShares);
            s.setProfit((int) ((inputPrice - matchedPrice) * inputShares));
            s.setPrice(inputPrice);
            s.setOrder(Stock.OrderType.SELL);

            historyTableHandler.insert(s);
            if (remainingShares == 0) {
                assetTableHandler.remove(s.getId());
            } else {
                s.setShares(remainingShares);
                s.setPrice(matchedPrice);
                assetTableHandler.update(s);
            }
        } else {
            Stock s = new Stock();
            s.setSymbol(inputSymbol);
            s.setPrice(inputPrice);
            s.setShares(inputShares);
            s.setOrder(Stock.OrderType.BUY);
            s.setProfit(0);

            historyTableHandler.insert(s);
            assetTableHandler.insert(s);
        }

        dismiss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();

        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Log.e(TAG, "Dismiss when pressing outside");
        dismiss();
    }

    private void addCancelButton(EditText edt, int id) {
        ImageButton btn = requireView().findViewById(id);

        btn.setVisibility(ImageButton.GONE);
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btn.setVisibility(s.length() > 0 ? ImageButton.VISIBLE : ImageButton.GONE);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt.getText().clear();
                edt.requestFocus();

                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edt, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }
}
