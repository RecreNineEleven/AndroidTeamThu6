package com.example.campusexpensemanagerse06304;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanagerse06304.adapter.BudgetAdapter;
import com.example.campusexpensemanagerse06304.database.BudgetDb;
import com.example.campusexpensemanagerse06304.model.Budget;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExpenseOverviewFragment extends Fragment {
    private TextView tvTotalSpent, tvRemaining;
    private RecyclerView rvCategoryBreakdown;
    private BudgetDb budgetDb;
    private BudgetAdapter budgetAdapter;
    private List<Budget> budgetList = new ArrayList<>();

    public ExpenseOverviewFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_overview, container, false);

        tvTotalSpent = view.findViewById(R.id.tvTotalSpent);
        tvRemaining = view.findViewById(R.id.tvRemainingBudget);
        rvCategoryBreakdown = view.findViewById(R.id.rvCategoryBreakdown);

        budgetDb = new BudgetDb(getContext());
        budgetList = budgetDb.getAllBudgets();

        double totalSpent = 0;
        double totalBudget = 0;

        for (Budget budget : budgetList){
            totalSpent += budget.getSpent();
            totalBudget += budget.getAmount();
        }

        double remaining = totalBudget - totalSpent;

        tvTotalSpent.setText(String.format(Locale.getDefault(), "Total spent : %,.of ", totalSpent));
        tvRemaining.setText(String.format(Locale.getDefault(), "Remaning Budget: %,. of", remaining));

        rvCategoryBreakdown.setLayoutManager(new LinearLayoutManager(getContext()));
        budgetAdapter = new BudgetAdapter(getContext(), budgetList, budgetDb, () -> refreshOverview());
        rvCategoryBreakdown.setAdapter(budgetAdapter);

        return view;
    }
    private void refreshOverview(){
        budgetList.clear();
        budgetList.addAll(budgetDb.getAllBudgets());
        budgetAdapter.notifyDataSetChanged();

        double totalSpent = 0;
        double totalBudget = 0;

        for (Budget budget : budgetList) {
            totalSpent += budget.getSpent();
            totalBudget += budget.getAmount();
        }
        double remaining = totalBudget - totalSpent;

        tvTotalSpent.setText(String.format(Locale.getDefault(), "Total spent : %,.of ", totalSpent));
        tvRemaining.setText(String.format(Locale.getDefault(), "Remaning Budget: %,. of", remaining));


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (budgetDb != null) budgetDb.close();
    }
}

