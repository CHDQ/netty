# _netty 实战关键笔记_

## 1. **_资源管理_**

- 章节 6.1
- 内存泄漏检测参数配置

> java -Dio.netty.leakDetectionLevel=ADVANCED 可以检测是否存在 ByteBuf 内存泄漏

- 泄漏检测级别

  |   级别   |                                                    描述                                                     |
  | :------: | :---------------------------------------------------------------------------------------------------------: |
  | DISABLED |                             禁用泄漏检测。只有在详尽的测试之后才应设置为这个值                              |
  |  SIMPLE  |                使用 1%的默认采样率检测并报告任何发现的泄露。这是默认级别，适合绝大部分的情况                |
  | ADVANCED |                     使用默认的采样率，报告所发现的任何的泄露以及对应的消息被访问的位置                      |
  | PARANOID | 类似于 ADVANCED，但是其将会对每次（对消息的）访问都进行采样。这对性能将会有很大的影响，应该只在调试阶段使用 |

## 2. **_pipeline_**

- 章节 6.2
- pipeline 的执行过程

  > 在 ChannelPipeline 传播事件时，它会测试 ChannelPipeline 中的下一个 ChannelHandler 的类型是否和事件的运动方向相匹配。如果不匹配， ChannelPipeline 将跳过该
  > ChannelHandler 并前进到下一个，直到它找到和该事件所期望的方向相匹配的为止。

- ChannelHandler 的用于修改 ChannelPipeline 的方法

  |                      名称                       |                                  描述                                  |
  | :---------------------------------------------: | :--------------------------------------------------------------------: |
  | AddFirst<br/>addBefore<br/>addAfter<br/>addLast |            将一个 ChannelHandler 添加到 ChannelPipeline 中             |
  |                     remove                      |            将一个 ChannelHandler 从 Channe****lPipeline 中移除             |
  |                     replace                     | 将 ChannelPipeline 中的一个 ChannelHandler 替换为另一个 ChannelHandler |

- 总结
    1. ChannelPipeline 保存了与 Channel 相关联的 ChannelHandler
    2. ChannelPipeline 可以根据需要，通过添加或者删除 ChannelHandler 来动态地修改
    3. ChannelPipeline 有着丰富的 API 用以被调用，以响应入站和出站事件
   
## 3. **_ChannelHandlerContext_**   

- 章节6.3
- ChannelHandlerContext 代表了 ChannelHandler 和 ChannelPipeline 之间的关联，每当有 ChannelHandler 添加到 ChannelPipeline 中时，都会创建 ChannelHandlerContext。 ChannelHandlerContext 的主要功能是管理它所关联的 ChannelHandler 和在同一个 ChannelPipeline 中的其他 ChannelHandler 之间的交互

## 4. **_EventLoop_** 

- 尽可能地重用 EventLoop，以减少线程创建所带来的开销