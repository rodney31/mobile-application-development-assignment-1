package com.example.mortgagepaymentcalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.mortgagepaymentcalculator.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    // Variables that will be used to store the TextView fields that will have values displayed through them
    private TextView principalTextView, termTextView, interestTextView, amortizationTextView, mortgagePaymentTextView, totalPaymentsTextView, interestPaidTextView, principalPaidTextView, remainingBalanceTextView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find and store the TextView fields that values get displayed to
        principalTextView = view.findViewById(R.id.principalAmountTextView);
        termTextView = view.findViewById(R.id.interestTermAmountTextView);
        interestTextView = view.findViewById(R.id.interestRateAmountTextView);
        amortizationTextView = view.findViewById(R.id.amortizationAmountTextView);
        mortgagePaymentTextView = view.findViewById(R.id.mortgagePaymentTextView);
        totalPaymentsTextView = view.findViewById(R.id.totalPaymentsTextView);
        interestPaidTextView = view.findViewById(R.id.interestPaidTextView);
        principalPaidTextView = view.findViewById(R.id.principalPaidTextView);
        remainingBalanceTextView = view.findViewById(R.id.balanceTextView);

        // Retrieve the values previously received and calculated in the first fragment through the bundle
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            double principal = bundle.getDouble("principal");
            double term = bundle.getDouble("term");
            double interest = bundle.getDouble("interest");
            double amortization = bundle.getDouble("amortization");
            double mortgagePayment = bundle.getDouble("mortgagePayment");
            double totalPaymentsOverTerm = bundle.getDouble("totalPaymentsOverTerm");
            double interestPaid = bundle.getDouble("interestPaid");
            double principalPaid = bundle.getDouble("principalPaid");
            double remainingBalance = bundle.getDouble("remainingBalance");

            // Display the values to the appropriate TextView field
            mortgagePaymentTextView.setText(String.format("$%.2f", mortgagePayment));
            principalTextView.setText(String.format("$%.2f", principal));
            termTextView.setText(String.format("%.0f years", term));
            interestTextView.setText(String.format("%.2f%%", interest));
            amortizationTextView.setText(String.format("%.0f years", amortization));
            totalPaymentsTextView.setText(String.format("$%.2f", totalPaymentsOverTerm));
            interestPaidTextView.setText(String.format("$%.2f", interestPaid));
            principalPaidTextView.setText(String.format("$%.2f", principalPaid));
            remainingBalanceTextView.setText(String.format("$%.2f", remainingBalance));
        }

        // Navigate back to the first fragment if the user wishes to perform more calculations
        binding.calculateAgainButton.setOnClickListener(v ->
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}