# 新增  
1. 昨天通过电话，解释了一下之前产生巨额报备的原因。  
    - 是因为把计算需求量的流程，放在释放虚拟机之前，而4月2日当天有巨量释放。  
    - 把顺序调换后，先释放，再算需求量，报备已经恢复正常。  
  
2. 第二个问题，程序只是根据销售数量进行判断，缺少预测。  
    报备必须要加上限定条件，比如当报备数量超过  
  
# 早上好！又是抱着网课打瞌睡的崭新一天  
我们尝试了很多笨方法，这是一个由笨方法组成的笨程序。  
  
# 写在前面  
浅薄之见，本次的程序分为两个部分：  
- 框架方面：正确计算收益和成本，分配时资源扣减、释放时资源返还。  
- 算法方面：最优化分配，计算出最佳补货数量。  
  
目前，程序框架部分基本完成，算法方面还有待完善。  
  
# 文件列表  
| 文件名 | 作用 |
| ----- | ----- |
| Elastic.java | 主程序 |
| Adjust.java | 把当前已经分配VM的物理机重新调优分布，本次程序没有用到  |
| Files.java | csv文件相关操作 |
| Log.java | 记录每天CPU需求数 |
| NC.java | 【核心】物理机对象，包含物理机的属性，和物理机相关的操作方法 |
| Price.java | 【核心】计算收益和成本 |
| Resource.java | 【核心】全局资源表，记录空闲CPU数、已分配CPU数 |
| Table.java | 价格表，方便用户修改单价，无其它作用  |
| Times.java | 时间相关操作 |
| VM.java | 【核心】虚拟机对象，包含虚拟机的属性，和虚拟机相关的操作方法 |
  
# 时间线  
| 时间 | 动作 |
| ----- | ----- |
| 每天00：00：00 | 启用报备好的物理机。 |
| 每天00：00：01 | 开始分配vm，每秒分配1台。  |
| ······ | ······ |
| 每天23：59：55 | 计算当天的收益。 |
| 每天23：59：56 | 释放今天到期的vm资源。  |
| 每天23：59：57 | 分配调优（本次没用到）。 |
| 每天23：59：58 | 确认要报备的数量。  |
| 每天23：59：59 | 计算当天的成本。 |
  
# 报备流程  
先把当日VM释放
需求CPU数 = 
总的来说，就是计算
# 输出结果  
- 报备NT-1-2数量：90台  
报备NT-1-4数量：90台  
报备NT-1-8数量：60台  
  
- 最后1天用了NT-1-2物理机：42台  
最后1天用了NT-1-4物理机：177台  
最后1天用了NT-1-8物理机：8台  
  
- 最后1天剩余NT-1-2物理机：16台  
最后1天剩余NT-1-4物理机：131台  
最后1天剩余NT-1-8物理机：7台  
  
- 总收益：6223438.32元  
总成本：16827798.40元  
收益率：-63.02%  
  
# 目前问题  
- 1-3月的数据，刚开始需求量少，报备情况较为理想。  
  4-6月数据时，刚开始需求量大，断供情况较为严重。
  两份数据差异很大
