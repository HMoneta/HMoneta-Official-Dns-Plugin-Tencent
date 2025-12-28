# HMoneta 官方腾讯 DNS 插件

[![Java Version](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![PF4J](https://img.shields.io/badge/PF4J-Plugin%20Framework-green.svg)](https://pf4j.org/)

HMoneta 官方开发的腾讯云 DNS 解析服务插件，基于 PF4J 插件框架，为 HMoneta 系统提供完整的腾讯云 DNS 记录管理功能。

## 🚀 特性

- ✅ **完整的 DNS 管理**：支持 DNS 记录的查询、创建、修改和删除
- 🔐 **安全的身份验证**：基于腾讯云 AccessKey 认证
- 🏗️ **插件化架构**：遵循 PF4J 标准，易于集成和扩展
- 📋 **多记录类型支持**：支持常见 DNS 记录类型
- 🔄 **自动化发布**：GitHub Actions 持续集成
- 📖 **完整的 API 集成**：基于腾讯云官方 SDK

## 📋 系统要求

- **Java**: JDK 17 或更高版本
- **Maven**: 3.6 或更高版本
- **HMoneta**: 兼容 HMoneta-Official-Plugin-Api 0.1.0
- **腾讯云**: 有效的 AccessKey 和 DNS Pod 权限

## 🛠️ 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/HMoneta/HMoneta-Official-Dns-Plugin-Tencent.git
cd HMoneta-Official-Dns-Plugin-Tencent
```

### 2. 构建项目

```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 构建插件包
mvn package
```

### 3. 获取插件文件

构建完成后，您将在 `target/` 目录找到：

- `HMoneta-Official-Dns-Plugin-Tencent-0.1.0.jar` - 插件 JAR 文件
- `HMoneta-Official-Dns-Plugin-Tencent-0.1.0-plugin.zip` - 完整插件包

## ⚙️ 配置说明

### 腾讯云配置

在使用插件前，您需要：

1. **获取腾讯云凭证**：
   - AccessKeyId
   - AccessKeySecret

2. **确保 DNS 权限**：
   - 腾讯云账号需要有 DNS Pod 的读写权限
   - 目标域名需要在 DNS Pod 中管理

### HMoneta 集成配置

1. 将 `plugin.zip` 文件放置到 HMoneta 插件目录
2. 重启 HMoneta 应用
3. 在 HMoneta 管理界面配置腾讯云凭证

## 📖 使用示例

### DNS 记录管理

插件提供以下 DNS 操作：

```java
// 查询 DNS 记录
List<DNSRecordInfo> records = dnsProvider.dnsCheck("example.com", "www");

// 创建/修改 DNS 记录
boolean success = dnsProvider.modifyDns("example.com", "www", "A", "192.168.1.1");

// 删除 DNS 记录
boolean deleted = dnsProvider.deleteDns("example.com", "www", "A");
```

### 支持的记录类型

- **A 记录**：IPv4 地址映射
- **AAAA 记录**：IPv6 地址映射  
- **CNAME 记录**：域名别名
- **MX 记录**：邮件交换记录
- **TXT 记录**：文本记录
- **NS 记录**：域名服务器记录

## 🏗️ 项目结构

```
HMoneta-Official-Dns-Plugin-Tencent/
├── src/main/
│   ├── assembly/assembly.xml          # Maven Assembly 配置
│   ├── java/fan/summer/hmoneta/official/dns/plugin/tencent/
│   │   └── TencentPlugin.java         # 主插件类
│   └── resources/
│       └── plugin.properties          # 插件元数据
├── .github/workflows/
│   └── maven.yml                      # CI/CD 配置
├── pom.xml                            # Maven 项目配置
├── .gitignore                         # Git 忽略配置
└── README.md                          # 项目说明文档
```

## 🔧 开发指南

### 环境搭建

1. **JDK 17+** 安装
2. **Maven 3.6+** 安装  
3. **IDE** 推荐使用 IntelliJ IDEA 或 Eclipse

### 开发流程

```bash
# 1. 开发新功能
# 编辑 src/main/java/fan/summer/hmoneta/official/dns/plugin/tencent/TencentPlugin.java

# 2. 运行测试
mvn test

# 3. 构建验证
mvn clean package

# 4. 代码检查（可选）
mvn checkstyle:check
```

### 添加新功能

1. 在 `TencentCloud` 类中实现新的 DNS 操作方法
2. 确保遵循 `HmDnsProviderPlugin` 接口规范
3. 添加适当的错误处理和日志记录
4. 编写对应的单元测试

## 📦 依赖说明

### 核心依赖

```xml
<!-- HMoneta 插件 API -->
<dependency>
    <groupId>fan.summer</groupId>
    <artifactId>HMoneta-Official-Plugin-Api</artifactId>
    <version>0.1.0</version>
    <scope>provided</scope>
</dependency>

<!-- PF4J Spring 支持 -->
<dependency>
    <groupId>org.pf4j</groupId>
    <artifactId>pf4j-spring</artifactId>
    <version>0.10.0</version>
    <scope>provided</scope>
</dependency>

<!-- 腾讯云 SDK -->
<dependency>
    <groupId>com.tencentcloudapi</groupId>
    <artifactId>tencentcloud-sdk-java</artifactId>
    <version>3.1.1356</version>
</dependency>
```

## 🚀 CI/CD

项目使用 GitHub Actions 实现自动化构建和发布：

- **自动构建**：每次推送触发 Maven 构建
- **自动测试**：运行单元测试确保代码质量  
- **自动发布**：版本标签推送时自动创建 GitHub Release

### 发布新版本

1. 更新 `pom.xml` 中的版本号
2. 提交更改并推送标签：
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
3. GitHub Actions 将自动构建并发布 Release

## 🐛 故障排除

### 常见问题

**Q: 插件加载失败**
```
A: 检查 HMoneta 版本兼容性，确保依赖的插件 API 版本匹配
```

**Q: 腾讯云 API 调用失败**
```
A: 验证 AccessKey 凭证正确性，确认 DNS Pod 权限设置
```

**Q: 构建时依赖解析错误**
```
A: 检查 Maven 配置，确保网络连接正常，可以访问 Maven 中央仓库
```

### 调试方法

1. **查看日志**：通过 SLF4J 日志定位问题
2. **API 测试**：使用腾讯云控制台验证权限
3. **网络检查**：确保可以访问 dnspod.tencentcloudapi.com

## 📄 许可证

本项目采用 Apache License 2.0 许可证。详情请查看 [LICENSE](LICENSE) 文件。

## 🤝 贡献

欢迎提交 Issue 和 Pull Request 来改进项目！

1. Fork 本仓库
2. 创建特性分支：`git checkout -b feature/AmazingFeature`
3. 提交更改：`git commit -m 'Add some AmazingFeature'`
4. 推送分支：`git push origin feature/AmazingFeature`
5. 创建 Pull Request

## 📞 联系我们

- **项目维护者**: HMoneta Team
- **项目地址**: [GitHub Repository](https://github.com/HMoneta/HMoneta-Official-Dns-Plugin-Tencent)
- **问题反馈**: [GitHub Issues](https://github.com/HMoneta/HMoneta-Official-Dns-Plugin-Tencent/issues)

---

<div align="center">
  <strong>HMoneta 官方腾讯 DNS 插件</strong><br>
  为您的 DNS 管理需求提供强大支持 🚀
</div>