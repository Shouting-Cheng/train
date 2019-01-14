package com.hand.hcf.app.train.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.hand.hcf.app.train.domain.TrainingReportHeader;
import com.hand.hcf.app.train.dto.TrainingReportDTO;
import com.hand.hcf.app.train.dto.TrainingReportHeaderDTO;
import com.hand.hcf.app.train.service.TrainingReportHeaderService;
import com.hand.hcf.core.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training/report/header")
public class TrainingReportHeaderController {

    @Autowired
    private TrainingReportHeaderService trainingReportHeaderService;

    @PostMapping
    public TrainingReportHeader createTrainingReportHeader(@RequestBody TrainingReportHeader header){
        trainingReportHeaderService.createTrainingReportHeader(header);
        return header;
    }

    @PutMapping
    public TrainingReportHeader updateTrainingReportHeader(@RequestBody TrainingReportHeader header){
        trainingReportHeaderService.updateTrainingReportHeader(header);
        return header;
    }

    @DeleteMapping("/{id}")
    public void deleteTrainingReportHeaderById(@PathVariable Long id) {
        trainingReportHeaderService.deleteTrainingReportHeaderById(id);
    }

    @GetMapping("/{id}")
    public TrainingReportHeaderDTO getTrainingReportHeaderById(@PathVariable Long id) {
        return trainingReportHeaderService.getTrainingReportHeaderById(id);
    }

    @PostMapping("/dto/save")
    public TrainingReportDTO saveTrainingReportDTO(@RequestBody TrainingReportDTO dto) {
        trainingReportHeaderService.saveTrainingReportDTO(dto);
        return dto;
    }

    @PostMapping("/batch/delete")
    public void deleteTrainingReportHeaderBatch(@RequestBody List<Long> list) {
        trainingReportHeaderService.deleteTrainingReportHeaderBatch(list);
    }


    @GetMapping("/query")
    public ResponseEntity<List<TrainingReportHeaderDTO>> getTrainingReportHeaderByCond(@RequestParam(required = false) Long applicationId,
                                                                                       @RequestParam(required = false) String businessCode,
                                                                                       @RequestParam(required = false) String reportStatus,
                                                                                       @RequestParam(required = false) Long companyId,
                                                                                       @RequestParam(value = "page",defaultValue = "0") int page,
                                                                                       @RequestParam(value = "size",defaultValue = "10") int size) {
        Page mybatisPage = PageUtil.getPage(page, size);
        List<TrainingReportHeaderDTO> headerDTOS = trainingReportHeaderService.getTrainingReportHeaderByCond(applicationId, businessCode,
                reportStatus, companyId, mybatisPage);
        HttpHeaders httpHeaders = PageUtil.getTotalHeader(mybatisPage);
        return new ResponseEntity<>(headerDTOS, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/dto/{headerId}/page")
    public ResponseEntity getTrainingReportHeaderDetailsById(@PathVariable Long headerId,
                                                             @RequestParam(value = "page",defaultValue = "0") int page,
                                                             @RequestParam(value = "size",defaultValue = "10") int size) {
        Page mybatisPage = PageUtil.getPage(page, size);
        TrainingReportDTO trainingReportDTO = trainingReportHeaderService.getTrainingReportHeaderDetailsById(headerId, mybatisPage);
        HttpHeaders headers = PageUtil.getTotalHeader(mybatisPage);
        return new ResponseEntity<>(trainingReportDTO, headers, HttpStatus.OK);
    }

    /**
     * 提交报销单
     * @param id
     * @return
     */
    @PutMapping("/submit/{id}")
    public TrainingReportHeader submitTrainingReportHeader(@PathVariable Long id) {
        return trainingReportHeaderService.submitTrainingReportHeader(id);
    }
}
