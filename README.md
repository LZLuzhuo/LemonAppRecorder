#LemonAppRecorder (柠檬APP记录器)
---
> - 该应用使用了MVP架构, 集成了RxJava, BusEvent, 以及对于沉浸式状态栏的处理.

## 简介:
> - 柠檬App记录器,这是一个Geek工具.  
> - 作为一个geek,恨不得把所有的app都玩一遍,可是记性不好,过段时间之后,记不得玩过什么app,结果悲催的是一些app下了一遍又一遍.于是弄个app记录器记录安装的app.  
> - 该应用默认将app分成三类:精品,一般,垃圾.当然也可以自定义分类.  
> - 该应用采用的是灰色主题,科技灰嘛,灰色代表技术,所以就用灰色咯.  
> - 该应用记录的所有应用信息的图标全部存在SD卡上,手机上装有垃圾清理软件的注意了,清理垃圾时请忽略该应用,不然的话,要是图标被清了就没地方找回来了.  

## 使用:
### 首页:
![](/screenshot/LemonAppRecorder1.jpeg)
> - 退出时,要快速按两下返回键才能退出,这是为了防止误操作而设计的.

### 添加App信息:
![](/screenshot/LemonAppRecorder2.gif)
> - 添加app信息.

### 修改App信息:
![](/screenshot/LemonAppRecorder3.gif)

### 导出(备份)
![](/screenshot/LemonAppRecorder4.gif)
> 导出到SD卡上的 `/Lemon/AppRecorder` 目录,导入到其他手机上时,需要将此文件夹下的所有文件拷贝到手机上.

### 导入(还原)
![](/screenshot/LemonAppRecorder5.gif)

### 清理数据
#### 清理分类信息
![](/screenshot/LemonAppRecorder6.gif)
> - 被清理的分类信息下不能存在App记录;
> - 如果存在App记录,则要先删除掉才能清理;

#### 清理App信息
![](/screenshot/LemonAppRecorder7.gif)

### 图标保存位置
![](/screenshot/LemonAppRecorder8.gif)
> - 图标默认保存到SD卡上;
> - 你可以根据自己的喜好,保存到手机内部存储,还是SD卡上;

### 统计数据
![](/screenshot/LemonAppRecorder9.gif)
> - 以饼图的方式显示数据;
> - 点击相应的饼页可以查看单个数量;
> - 分类数量建议不要超过5个.

### 已安装提示
![](/screenshot/LemonAppRecorder10.gif)
> - 对已经记录的应用再次安装时,弹出提示

## 下载
#### [应用宝(腾讯)](http://android.myapp.com/myapp/detail.htm?apkName=me.luzhuo.lemonapprecorder)

## 关于作者

Luzhuo  
Email: `LZ.Luzhuo@gmail.com`  
Blog: `http://blog.csdn.net/Rozol/article/details/50485155`  


## License

	Copyright 2016 Luzhuo. All rights reserved.
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	    http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.