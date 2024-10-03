package com.example.mortgagepaymentcalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.mortgagepaymentcalculator.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    // Variables that will be used to store the EditText fields that need to have values extracted from them
    private EditText principalTextField, termTextField, interestTextField, amortizationTextField;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find and store the EditText fields that the user enters values into
        principalTextField = view.findViewById(R.id.principalEditText);
        termTextField = view.findViewById(R.id.interestTermEditText);
        interestTextField = view.findViewById(R.id.interestRateEditText);
        amortizationTextField = view.findViewById(R.id.amortizationEditText);

        // Setting up a listener that checks if the Calculate Payments button gets pressed
        binding.calculateButton.setOnClickListener(v -> {

            double principal = getPrincipal();

            double term = getTerm();

            double interest = getInterest();

            double amortization = getAmortization();

            double mortgagePayment = calculateMortgagePayment(principal, interest, amortization);

            double totalPaymentsOverTerm = calculateTotalPaymentsOverTerm(mortgagePayment, term);

            double remainingBalance = calculateRemainingBalance(principal, interest, term, mortgagePayment);

            double interestPaid = calculateInterestPaid(totalPaymentsOverTerm, principal, remainingBalance);

            double principalPaid = calculatePrincipalPaid(totalPaymentsOverTerm, interestPaid);

            // Put all the received and calculated values into a bundle that is able to be accessed in the second fragment
            Bundle bundle = new Bundle();
            bundle.putDouble("principal", principal);
            bundle.putDouble("term", term);
            bundle.putDouble("interest", interest);
            bundle.putDouble("amortization", amortization);
            bundle.putDouble("mortgagePayment", mortgagePayment);
            bundle.putDouble("totalPaymentsOverTerm", totalPaymentsOverTerm);
            bundle.putDouble("interestPaid", interestPaid);
            bundle.putDouble("principalPaid", principalPaid);
            bundle.putDouble("remainingBalance", remainingBalance);

            // Navigate to the second fragment to display results to the user. The bundle also gets passed to the second fragment
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Retrieve the principal value from the appropriate EditText field
    public double getPrincipal() {

        return getValue(principalTextField);
    }

    // Retrieve the interest term value from the appropriate EditText field
    public double getTerm() {

        return getValue(termTextField);
    }

    // Retrieve the interest rate value from the appropriate EditText field
    public double getInterest() {

        return getValue(interestTextField);
    }

    // Retrieve the amortization period value from the appropriate EditText field
    public double getAmortization() {

        return getValue(amortizationTextField);
    }

    // Getter method for the different values
    public double getValue(EditText textField) {
        try
        {
            return Double.parseDouble(textField.getText().toString());
        }
        catch (NumberFormatException e) // Inform user when invalid input has been provided
        {
            displayErrorMessage("Input fields were left blank. Please try calculating again.");

            return 0; // Return 0 as a default value
        }
    }

    // Send a toast notification to user indicating that invalid input was provided, and to try again
    public void displayErrorMessage(String message)
    {
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(this.getContext(), message, duration);
        toast.show();
    }

    // The monthly mortgage payment gets calculated and returned
    public double calculateMortgagePayment(double principal, double interest, double amortization) {
        double semiAnnualInterestRate, monthlyInterestRate, numberOfPayments, mortgagePayment;

        semiAnnualInterestRate = Math.pow((1 + (interest / 200.0)), 2) - 1;
        monthlyInterestRate = Math.pow((1 + semiAnnualInterestRate), 1.0 / 12.0) - 1;
        numberOfPayments = amortization * 12.0;

        mortgagePayment = principal * ((monthlyInterestRate * Math.pow((1 + monthlyInterestRate), numberOfPayments)) / (Math.pow((1 + monthlyInterestRate), numberOfPayments) - 1));

        return mortgagePayment;
    }

    // The total financial contribution is calculated by determining the total number of payments made during the term
    public double calculateTotalPaymentsOverTerm(double mortgagePayment, double term) {
        return mortgagePayment * term * 12.0;
    }

    // The remaining balance on the user's mortgage is calculated
    public double calculateRemainingBalance(double principal, double interest, double term, double mortgagePayment)
    {
        double remainingBalance, semiAnnualInterestRate, monthlyInterestRate, interestTracker, principalTracker;

        remainingBalance = principal;

        semiAnnualInterestRate = Math.pow((1 + (interest / 200.0)), 2) - 1;
        monthlyInterestRate = Math.pow((1 + semiAnnualInterestRate), 1.0 / 12.0) - 1;

        for (int i = 0; i < term * 12; i++)
        {
            interestTracker = remainingBalance * monthlyInterestRate;
            principalTracker = mortgagePayment - interestTracker;
            remainingBalance = remainingBalance - principalTracker;
        }

        return remainingBalance;
    }

    // The interest the user paid over the term is calculated
    public double calculateInterestPaid(double totalPaymentsOverTerm, double principal, double remainingBalance)
    {
        return totalPaymentsOverTerm - (principal - remainingBalance);
    }

    // The principal the user paid over the term is calculated
    public double calculatePrincipalPaid(double totalPaymentsOverTerm, double interestPaid)
    {
        return totalPaymentsOverTerm - interestPaid;
    }
}