package com.hand.hcf.app.train.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hand.hcf.app.client.com.CompanyCO;
import com.hand.hcf.app.client.com.CompanyClient;
import com.hand.hcf.app.client.department.DepartmentCO;
import com.hand.hcf.app.client.department.DepartmentClient;
import com.hand.hcf.app.client.org.OrganizationClient;
import com.hand.hcf.app.client.org.SysCodeValueCO;
import com.hand.hcf.app.client.sob.SetOfBooksInfoCO;
import com.hand.hcf.app.client.sob.SobClient;
import com.hand.hcf.app.client.user.UserCO;
import com.hand.hcf.app.client.user.UserClient;
import com.hand.hcf.app.train.domain.TrainingReportHeader;
import com.hand.hcf.app.train.dto.TrainingReportDTO;
import com.hand.hcf.app.train.dto.TrainingReportHeaderDTO;
import com.hand.hcf.app.train.enums.TrainingReportStatus;
import com.hand.hcf.app.train.persistence.TrainingReportHeaderMapper;
import com.hand.hcf.app.train.utils.RespCode;
import com.hand.hcf.core.exception.BizException;
import com.hand.hcf.core.service.BaseService;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
@CacheConfig(cacheNames = "TrainingReportHeader")
public class TrainingReportHeaderService  extends BaseService<TrainingReportHeaderMapper,TrainingReportHeader> {

    @Autowired
    private TrainingReportHeaderMapper trainingReportHeaderMapper;
    @Autowired
    private TrainingReportLineService trainingReportLineService;
    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private CompanyClient companyClient;
    @Autowired
    private SobClient sobClient;
    @Autowired
    private DepartmentClient departmentClient;
    @Autowired
    private UserClient userClient;
    @Autowired
    private OrganizationClient organizationClient;

    @CachePut(key = "#header.id")
    public TrainingReportHeader createTrainingReportHeader(TrainingReportHeader header) {

        if (header.getId() != null) {
            throw new BizException(RespCode.ID_NOT_NULL);
        }
        trainingReportHeaderMapper.insert(header);
        return header;
    }

    @CachePut(key = "#header.id")
    public TrainingReportHeader updateTrainingReportHeader(TrainingReportHeader header) {

        if (header.getId() == null) {
            throw new BizException(RespCode.ID_NULL);
        }
        trainingReportHeaderMapper.updateAllColumnById(header);
        return header;
    }

    public TrainingReportHeader saveTrainingReportHeader(TrainingReportHeader header) {

        if (header.getId() == null) {
            createTrainingReportHeader(header);
        }else {
            updateTrainingReportHeader(header);
        }
        return header;
    }

    @CacheEvict(key = "#id")
    public void deleteTrainingReportHeaderById(Long id) {

        TrainingReportHeader header = trainingReportHeaderMapper.selectById(id);
        if (header == null) {
            throw new BizException(RespCode.DATA_NOT_EXISTS);
        }
        header.setBusinessCode(header.getBusinessCode() + "_DELETED_" + RandomStringUtils.randomNumeric(6));
        trainingReportHeaderMapper.updateAllColumnById(header);
        trainingReportLineService.deleteTrainingReportLineByHeaderId(header.getId());
    }

    public List<TrainingReportHeader> createTrainingReportHeaderBatch(List<TrainingReportHeader> headers) {
        headers.stream().forEach(header ->{
            createTrainingReportHeader(header);
        });
        return headers;
    }

    @Transactional
    public void deleteTrainingReportHeaderBatch(List<Long> list) {
        list.forEach(this::deleteTrainingReportHeaderById);
    }

    public TrainingReportDTO saveTrainingReportDTO(TrainingReportDTO dto){
        saveTrainingReportHeader(toDomain(dto.getHeader()));
        dto.getLines().stream().forEach(line -> {
            line.setHeaderId(dto.getHeader().getId());
            trainingReportLineService.saveTrainingReportLine(line);
        });
        return dto;
    }

    @Transactional(readOnly = true)
//    @Cacheable(key = "#id")
    public TrainingReportHeaderDTO getTrainingReportHeaderById(Long id) {
        TrainingReportHeader header = trainingReportHeaderMapper.selectById(id);
        return toDTO(header);
    }

    @Transactional(readOnly = true)
    public List<TrainingReportHeaderDTO> getTrainingReportHeaderByCond(Long applicationId,
                                                                       String businessCode,
                                                                       String reportStatus,
                                                                       Long companyId,
                                                                       Page page) {
        List<TrainingReportHeader> headerList = trainingReportHeaderMapper.selectPage(page, new EntityWrapper<TrainingReportHeader>()
                .eq(applicationId != null, "application_id", applicationId)
                .like(businessCode != null, "business_code", businessCode)
                .eq(reportStatus!= null, "report_status", reportStatus)
                .eq(companyId != null, "company_id", companyId)
                .eq("deleted", false));
        return toDTO(headerList);
    }

    public TrainingReportDTO getTrainingReportHeaderDetailsById(Long id, Page page) {
        TrainingReportDTO trainingReportDTO = new TrainingReportDTO();
        trainingReportDTO.setHeader(toDTO(trainingReportHeaderMapper.selectById(id)));
        trainingReportDTO.setLines(trainingReportLineService.getTrainingReportLineByHeaderId(id, page));
        return trainingReportDTO;
    }

    public TrainingReportHeader submitTrainingReportHeader(Long id) {
        TrainingReportHeader header = trainingReportHeaderMapper.selectById(id);
        if (header == null) {
            throw new BizException(RespCode.DATA_NOT_EXISTS);
        }
        header.setReportStatus(TrainingReportStatus.SUBMIT);
        trainingReportHeaderMapper.updateAllColumnById(header);
        return header;
    }

    public TrainingReportHeaderDTO toDTO(TrainingReportHeader domain){
        TrainingReportHeaderDTO dto;
        //设置相同属性
        dto = mapperFacade.map(domain, TrainingReportHeaderDTO.class);

        //转化其他属性

        CompanyCO companyCO = companyClient.getById(domain.getCompanyId());
        if (companyCO != null) {
            dto.setCompanyName(companyCO.getName());
        }

        SetOfBooksInfoCO setOfBooksInfoCO = sobClient.getSetOfBooksById(domain.getSetOfBooksId());
        if (setOfBooksInfoCO != null) {
            dto.setSetOfBooksName(setOfBooksInfoCO.getSetOfBooksName());
        }

        DepartmentCO departmentCO = departmentClient.getDepartmentById(domain.getUnitId());
        if (departmentCO != null) {
            dto.setUnitName(departmentCO.getName());
        }

        UserCO userCO = userClient.getById(domain.getApplicationId());
        if (userCO != null) {
            dto.setApplicationName(userCO.getFullName());
        }

        SysCodeValueCO sysCodeValueCO = organizationClient.getSysCodeValueByCodeAndValue("2028", domain.getReportStatus().getId().toString());
        if (sysCodeValueCO != null) {
            dto.setReportStatusDesc(sysCodeValueCO.getName());
        }

        return dto;
    }

    public List<TrainingReportHeaderDTO> toDTO(List<TrainingReportHeader> domains){
        List<TrainingReportHeaderDTO> dtos = new ArrayList<>();
        Map<Long,String> userMap = new HashMap<>();

        Map<String,String> statusMap;
        List<SysCodeValueCO> codeValueCOS = organizationClient.listAllSysCodeValueByCode("2028");
        statusMap = codeValueCOS.stream().collect(Collectors.toMap(SysCodeValueCO::getValue, SysCodeValueCO::getName, (k1, k2) ->k1));

        for (TrainingReportHeader domain : domains){
            TrainingReportHeaderDTO dto;
            //设置相同属性
            dto = mapperFacade.map(domain, TrainingReportHeaderDTO.class);

            //转化其他属性
            dto.setReportStatus(domain.getReportStatus().getId());
            //先判断userMap存在数据不，不存在就去查
            if (!userMap.containsKey(domain.getApplicationId())) {
                // 获取用户名称
                UserCO userCO = userClient.getById(domain.getApplicationId());
                if (userCO != null) {
                    userMap.put(domain.getApplicationId(), userCO.getFullName());
                }
            }
            dto.setApplicationName(userMap.get(domain.getApplicationId()));

            dto.setReportStatusDesc(statusMap.get(domain.getReportStatus().getId()));
            dtos.add(dto);
        }
        return dtos;
    }

    public static TrainingReportHeader toDomain(TrainingReportHeaderDTO dto){
        if (dto != null){
            TrainingReportHeader domain = new TrainingReportHeader();
            BeanUtils.copyProperties(dto, domain);

            domain.setReportStatus(TrainingReportStatus.parse(dto.getReportStatus()));

            return domain;
        }
        return null;
    }

    public static List<TrainingReportHeader> toDomain(List<TrainingReportHeaderDTO> dtos){
        if (dtos != null){
            List domains = new ArrayList<TrainingReportHeader>();
            for (TrainingReportHeaderDTO dto : dtos){
                domains.add(toDomain(dto));
            }
            return domains;
        }
        return null;
    }
}
