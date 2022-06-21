
#1.前置知识
## 1.1 dependencyManagement
```
在maven多模块项目中(即聚合工程)，可以在项目父pom文件中，使用dependencyManagement定义所有模块需要用到的依赖版本。
dependencyManagement只会影响现有依赖的配置，但不会引入依赖。
在父pom(主)中严禁直接使用depandencys预定义依赖，否则子model会自动继承depandencies中所有预定义依赖。
maven的继承与Java相同，单继承，子model中只能出现一个parent标签，对应一个pom文件的dependencyManagement。
此时，如果dependencyManagement中预定义太多的依赖，会造成pom文件过长，而且很乱。
```

## 1.2 BOM（Bill of Materials）
```
本质是一个普通的POM文件，只是在这个POM中，罗列的是一个工程的所有依赖和其对应的版本。
该文件一般被其它工程使用，当其它工程引用BOM中的依赖时，不用指定具体的版本，会自动使用BOM对应的jar版本。
BOM的维护方负责版本升级，并保证BOM中定义的jar包版本之间的兼容性。
一个pom文件中可以引用多个bom。
```

### 1.2.1 BOM 文件示例
```
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.niceshot</groupId>
    <artifactId>test-BOM</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <!--注意-->
    <packaging>pom</packaging>
    <!--注意-->
    <name>Test-BOM</name>
    <description>parent pom</description>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>test</groupId>
                <artifactId>a</artifactId>
                <version>1.2</version>
            </dependency>
            <dependency>
                <groupId>test</groupId>
                <artifactId>b</artifactId>
                <version>1.0</version>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>test</groupId>
                <artifactId>c</artifactId>
                <version>1.0</version>
                <scope>compile</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

### 1.2.2 BOM引用示例
```
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.niceshot</groupId>
                <artifactId>test-BOM</artifactId>
                <version>0.0.1-SNAPSHOT</version>
                <type>pom</type>
                <!-- scope=import 只能用在dependencyManagement 里面,
                    且仅用于 type=pom 的 dependency -->
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

## 1.3 冲突版本选定规则
```
依赖的版本可在多处配置，当出现版本冲突时，具体使用哪一个版本的优先顺序
1、直接在当前工程中显示指定的版本
2、parent中配置的父工程使用的版本
3、在当前工程中通过dependencyManagement引入的BOM清单中的版本，当引入的多个BOM都有对应jar包时，先引入的BOM生效
4、上述三个地方都没配置，则启用依赖调解 dependency mediation
   依赖调节遵循 —— 最短最近原则
```

### 1.3.1 最短路径：取决于路径的长短
```
A -> B -> C -> D 1.4

A -> E -> D 1.0

使用D的1.0版本。
```

### 1.3.2 最近原则：取决于依赖的先后
```
A -> B -> D 1.1

A -> C -> D 1.3

使用D的1.1版本。
```

## 1.4 scope 
### 1.4.1 scope —— provided
```
<scope>provided</scope>

scope默认为compile，项目在编译，测试，运行阶段都会引入依赖中需要的依赖 。

provided的意思是：
容器中已经provide了这个依赖。
provided会认为目标容器已经引入了依赖中需要的依赖（例: 运行和测试 servlet API, 实际项目运行时容器会提供 API）。

比如，项目ProjectABC 中有一个类叫C,而这个C1中会import这个portal-impl的artifact中的类B，那么在编译阶段，我们肯定需要这个B，否则C通不过编译，因为scope设置为provided，所以编译阶段起作用，所以C正确的通过了编译。测试阶段类似。

那么最后我们要把ProjectABC部署到Liferay服务器上，这时候，我们到$liferay-tomcat-home\webapps\ROOT\WEB-INF\lib下发现，里面已经有了一个portal-impl.jar了，换句话说，容器已经提供了这个artifact对应的jar。所以，这个C类直接可以用容器提供的portal-impl.jar中的B类，而不会出任何问题。

实际打包检测发现：当我们用maven install生成最终的构件包ProjectABC.war后，在其下的WEB-INF/lib中，会包含我们被标注为scope=compile的构件的jar包，而不会包含我们被标注为scope=provided的构件的jar包。
```


```参考资料```
dubbo-bom
https://blog.csdn.net/xue_xiaofei/article/details/118037269