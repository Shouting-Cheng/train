# demo




git clone -b development https://code.choerodon.com.cn/hand-rongjing-hcf-train/train.git








### 本地启动项目配置环境变量：
eureka.client.enabled=false

env=dev

hcf.application.base.url=http://47.101.145.62:9081/base (注:如需要其他模块，可将变量及变量值中的base替换为相应applicationName)

dev_meta=http://115.159.108.80:8180

spring.redis.host=localhost

security.oauth2.client.access-token-uri=http://47.101.145.62:9081/auth/oauth/token

security.oauth2.resource.user-info-uri=http://47.101.145.62:9081/auth/api/check_token
