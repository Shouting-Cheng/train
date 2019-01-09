

INSERT INTO bgt_parameter ( id, parameter_code, parameter_name, created_date, created_by, last_updated_date, last_updated_by, version_number)
VALUES
  ( 3, 'UNMAPPED_BUDGET_ITEM', '未映射预算项目处理方式', ${now}, 1, ${now}, 1, 1 );
  

INSERT INTO  bgt_parameter_values ( id ,  parameter_value_code ,  parameter_value_name ,  parameter_id ,  parameter_code ,  created_date ,  created_by ,  last_updated_date ,  last_updated_by ,  paramete_default_value ,  version_number) VALUES (5, 'NO_BUDGET_CONTROL', '不控制预算', 3, 'UNMAPPED_BUDGET_ITEM', ${now}, 1, ${now}, 1, 1, 1);
INSERT INTO  bgt_parameter_values ( id ,  parameter_value_code ,  parameter_value_name ,  parameter_id ,  parameter_code ,  created_date ,  created_by ,  last_updated_date ,  last_updated_by ,  paramete_default_value ,  version_number) VALUES (6, 'BUDGET_CHECK_ERROR', '预算校验错误', 3, 'UNMAPPED_BUDGET_ITEM', ${now}, 1, ${now}, 1, 0, 1);



INSERT INTO bgt_parameter_setting SELECT
bo.id budget_org_id,
bo.tenant_id tenant_id,
bo.set_of_books_id set_of_books_id,
b.id AS parameter_id,
b.parameter_code AS parameter_code,
( SELECT bpv.parameter_value_code FROM bgt_parameter_values bpv WHERE bpv.parameter_id = b.id AND bpv.paramete_default_value = 1 ) parameter_value_code,
${now} AS created_date,
1 AS created_by,
${now} AS last_updated_date,
1 AS last_updated_by,
(SELECT COUNT(*)  from bgt_organization bor WHERE bo.id > bor.id) + (b.id *(select count(1) from bgt_organization))  AS id,
1 AS version_number
FROM
	bgt_parameter b,
	bgt_organization bo
where b.id = 3;