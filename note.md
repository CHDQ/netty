# *netty实战关键笔记*

## 1. ***资源管理***
+ 内存泄漏检测参数配置

    >章节6.1.6中存在详细说明

    >java -Dio.netty.leakDetectionLevel=ADVANCED 可以检测是否存在ByteBuf内存泄漏

+ 泄漏检测级别

    | 级别 | 描述 |
    |:---: | :---:|
    |DISABLED|禁用泄漏检测。只有在详尽的测试之后才应设置为这个值|
    |SIMPLE|使用 1%的默认采样率检测并报告任何发现的泄露。这是默认级别，适合绝大部分的情况|
    |ADVANCED|使用默认的采样率，报告所发现的任何的泄露以及对应的消息被访问的位置|
    |PARANOID|类似于 ADVANCED，但是其将会对每次（对消息的）访问都进行采样。这对性能将会有很大的影响，应该只在调试阶段使用|

## 2. pipeline
+ pipeline的执行过程
    >在 ChannelPipeline 传播事件时，它会测试 ChannelPipeline 中的下一个 ChannelHandler 的类型是否和事件的运动方向相匹配。如果不匹配， ChannelPipeline 将跳过该
    ChannelHandler 并前进到下一个，直到它找到和该事件所期望的方向相匹配的为止。

