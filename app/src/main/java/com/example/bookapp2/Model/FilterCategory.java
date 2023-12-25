package com.example.bookapp2.Model;

import android.widget.Filter;

import com.example.bookapp2.Adapter.BookListAdapter;

import java.util.ArrayList;

public class FilterCategory extends Filter {

    ArrayList<Book> filterList;

    BookListAdapter bookListAdapter;

    public FilterCategory(ArrayList<Book> filterList, BookListAdapter bookListAdapter) {
        this.filterList = filterList;
        this.bookListAdapter = bookListAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        if(charSequence != null && charSequence.length() > 0){
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<Book> filterModels = new ArrayList<>();
            for(int i =0; i<filterList.size();i++){
                if(filterList.get(i).getBookName().toUpperCase().contains(charSequence)){
                    filterModels.add(filterList.get(i));
                }
            }

            results.count = filterModels.size();
            results.values = filterModels;

        }
        else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        bookListAdapter.OfferList = (ArrayList<Book>) filterResults.values;
        bookListAdapter.notifyDataSetChanged();
    }
}
