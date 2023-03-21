package com.project.havewemet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.havewemet.model.AppUser;
import com.project.havewemet.model.Model;
import com.project.havewemet.model.Status;

import java.util.List;

public class StatusListViewModel extends ViewModel {
    private final LiveData<List<Status>> statusData = Model.instance().getAllStatuses();
    public LiveData<List<Status>> getStatusData() {
        return statusData;
    }

}
