package com.hand.hcf.app.train.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hand.hcf.app.train.domain.TrainingReportLine;
import com.hand.hcf.app.train.persistence.TrainingReportLineMapper;
import com.hand.hcf.app.train.utils.RespCode;
import com.hand.hcf.core.exception.BizException;
import com.hand.hcf.core.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class TrainingReportLineService extends BaseService<TrainingReportLineMapper,TrainingReportLine> {

    @Autowired
    private TrainingReportLineMapper lineMapper;
    @Autowired
    private TrainingReportHeaderService headerService;


    public TrainingReportLine createTrainingReportLine(TrainingReportLine line){

        if (line.getId() != null) {
            throw new BizException(RespCode.ID_NOT_NULL);
        }
        if (line.getHeaderId() != null && headerService.selectById(line.getHeaderId()) != null) {
            lineMapper.insert(line);
        } else {
            throw new BizException(RespCode.DATA_NOT_EXISTS);
        }

        return line;
    }

    public TrainingReportLine updateTrainingReportLine(TrainingReportLine line) {
        if (line.getId() == null) {
            throw new BizException(RespCode.ID_NULL);
        }
        Long dbHeaderId = lineMapper.selectById(line.getId()).getHeaderId();
        if (line.getHeaderId() != null && line.getHeaderId().equals(dbHeaderId)) {
            lineMapper.updateById(line);
        } else {
            throw new BizException(RespCode.DATA_NOT_EXISTS);
        }

        return line;
    }

    public void deleteTrainingReportLineById(Long id) {
        TrainingReportLine line = lineMapper.selectById(id);
        if (line == null) {
            throw new BizException(RespCode.DATA_NOT_EXISTS);
        }
        lineMapper.updateById(line);
    }

    @Transactional(readOnly = true)
    public TrainingReportLine getTrainingReportLineById(Long id) {
        return lineMapper.selectById(id);
    }

    public void deleteTrainingReportLineByHeaderId(Long headerId) {
        List<TrainingReportLine> lines = getTrainingReportLineByHeaderId(headerId);
        lines.stream().forEach(line -> {
            deleteTrainingReportLineById(line.getId());
        });
    }

    public List<TrainingReportLine> getTrainingReportLineByHeaderId(Long headerId, Page page) {
        return lineMapper.selectPage(page, new EntityWrapper<TrainingReportLine>()
                .eq(headerId != null, "header_id", headerId)
                .eq("deleted", false));
    }

    public List<TrainingReportLine> getTrainingReportLineByHeaderId(Long headerId) {
        return lineMapper.selectList(new EntityWrapper<TrainingReportLine>()
                .eq(headerId != null, "header_id", headerId)
                .eq("deleted", false));
    }






    public TrainingReportLine saveTrainingReportLine(TrainingReportLine line){
        if (line.getId() == null) {
            createTrainingReportLine(line);
        }else {
            updateTrainingReportLine(line);
        }
        return line;
    }

    public List<TrainingReportLine> createTrainingReportLineBatch(List<TrainingReportLine> lines) {
        lines.stream().forEach(line -> {
            createTrainingReportLine(line);
        });
        return lines;
    }

    public List<TrainingReportLine> updateTrainingReportLineBatch(List<TrainingReportLine> lines) {
        lines.stream().forEach(line -> {
            updateTrainingReportLine(line);
        });
        return lines;
    }

    public List<TrainingReportLine> saveTrainingReportLineBatch(List<TrainingReportLine> lines) {
        lines.stream().forEach(line -> {
            saveTrainingReportLine(line);
        });
        return lines;
    }

    public void deleteTrainingReportLineBatch(List<Long> list) {
        this.delete(new EntityWrapper<TrainingReportLine>().in("id", list));
    }
}
