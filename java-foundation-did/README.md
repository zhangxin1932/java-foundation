说明（重要）：
(1)	关于时间位、机器位、序列位的配置
雪花算法中除去最高位符号位占用1bit，剩余63bit可以由业务侧根据实际需要自定义配置，
目前ID组件中对于时间位、机器位、序列位的默认分配如下：
timeBits: 时间位（单位秒），默认31 bits,  大概可用68年（2^31/365*24*60*60）
workerBits: 机器位，默认20 bits,按照200台机器，5天重启一次，可重启71年（2^20/(365/5*200）)
sequenceBits: 序列位， 默认12 bits, 最大值4096，即同一台机器同一秒内可生成4096个id

(2)	关于dataCenterId的配置
dataCenterId主要用于区别多机房场景，
如果业务侧不需要通过ID本身来区分机房，比如业务侧通过在ID上拼接机房标识来区分不同机房，则可以不用配置dataCenterId;
如果业务侧需要通过ID来区分机房，那么ID组件目前采用的方案是从timeBits抠出1bit，从workerBits抠出3bit，共4bit作为机房位，此时ID组件中时间位、机房位，机器位、序列位的默认分配如下（不推荐）：
timeBits: 时间位（单位秒），默认30 bits,  大概可用34年（2^30/365*24*60*60）
dataCenterBit：机房位，默认4bit，最多支持16个机房，dataCenterId合法取值0~15
workerBits: 机器码位，默认17 bits，按照200台机器，5天重启一次，可重启8.9年（2^17/(365/5*200）)
sequenceBits: 序列位， 默认12 bits, 最大值4096，即同一台机器同一秒内可生成4096个id

(3)	关于epochStr的配置
基准时间用于计算时间差，配置格式”yyyy-MM-dd HH:mm:ss”,配置后不允许变更，不可以配置未来时间

(4)	关于tableName的配置
tableName主要用于获取机器码，如果同一Spring容器中如果有多个业务，
且多个业务不想共享相同的ID号段，则可以为不同的业务配置不同ID生成器Bean，
并新建不同的机器码表通过tableName属性注入对应的ID生成器Bean

https://cloud.tencent.com/developer/article/1634640