# PDFAndroid
程序猿众所周知，Google由于退出中国市场，只有翻墙才能访问。从而导致webview不能在线打开PDF，然而iOS可以在线预览PDF文件。
于是就开始了自己的代码之路，在线打开PDF。特别感谢大神提供的打开PDF本地文件，于是改造一番! 
https://github.com/JoanZapata/android-pdfview

----

## 截图
![](https://github.com/lihaodong/PDFAndroid/blob/master/images/image.gif)
## 使用方法
### Gradle
```groovy
compile 'com.lihaodong.pdf:hpdfutil:1.0'
```
### Maven
```groovy
<dependency>
  <groupId>com.lihaodong.pdf</groupId>
  <artifactId>hpdfutil</artifactId>
  <version>1.0</version>
  <type>pom</type>
</dependency>
```
### Eclipse ADT

放弃治疗。
## 开始集成
### 在你的layout包含PDFView
```
<com.lihaodong.pdf.PDFView
        android:id="@+id/pdfView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
### 网络调用
```
//文件名默认为url后缀，储存路径为SDCard/Android/data/你的应用的包名/files/ Download/pdf/
pdfView .fromUrl("pdf地址",网络回调HttpListener);
//储存路径为SDCard/Android/data/你的应用的包名/files/ Download/pdf/
pdfView .fromUrl("pdf地址","pdf文件名",网络回调HttpListener);
pdfView .fromUrl("pdf地址","pdf文件名","文件储存路径"，网络回调HttpListener);
```
### 本地调用
```
pdfView.fromFile(file)//加载文件对象
pdfView.fromAsset(fileName)//记载asset目录下的文件，根据文件名加载

加载本地PDF，最后一步load()方法进行加载显示文件！

```
## 需要的权限
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.INTERNET" />
```
## 适配Android6.0

关于6.0适配，请自行在调用API时申请WRITE_EXTERNAL_STORAGE权限，可以参加demo中的代码
## 友好的调试模式

```
java
```
## License
```text
Copyright 2017 Li Haodong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```




