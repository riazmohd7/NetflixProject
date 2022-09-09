package com.netflix.service;

import com.netflix.Mapper.ShowMapper;
import com.netflix.accessor.ShowAccessor;
import com.netflix.accessor.model.ShowDTO;
import com.netflix.controller.model.ShowOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShowService {

    @Autowired
    ShowAccessor showAccessor;

    @Autowired
    ShowMapper showMapper;

    public List<ShowOutput> getShowByName(final String showName){
        List<ShowDTO>  showDTOList = showAccessor.getShowByName(showName);
        List<ShowOutput> showOutputList = new ArrayList<>();
        for(ShowDTO showDTO: showDTOList){
            showOutputList.add(showMapper.mapShowDtoToOutput(showDTO));
        }
        return showOutputList;
    }
}
