package com.project.havewemet;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.project.havewemet.model.External;
import com.project.havewemet.model.ExternalAPI;
import com.project.havewemet.model.ExternalModel;

import java.util.List;
public class ApiViewModelFragment extends ViewModel {
    private LiveData<List<External>> dataByTitle = ExternalModel.instance().searchEducationByTitle("title");
    LiveData<List<External>> getData(){
        return dataByTitle;
    }

}
