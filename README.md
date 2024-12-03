# 计算机网络五层协议模拟

基本完成数据从应用层封装到物理层得到一个bin文件，然后可以将bin文件解封装到应用层获得封装的数据

## test

### 功能

- `NetworkSimulator`:实现了五层协议的封装和解封装，可以通过调用sendData和receiveData方法实现数据的传输
- `Encapsulation`:实现了数据的封装
- `Decapsulation`:实现了数据的解封装
- `complex_physical_layer_data.bin`:封装后的数据

### 使用

1. 在`Encapsulation`类中修改数据，然后运行,可以得到封装后的数据`complex_physical_layer_data.bin`
2. 运行`Decapsulation`类，可以得到解封装后的数据，会在控制带输出封装的数据

## demo

### 功能

- `NetworkSimulator`：实现了五层协议的封装和解封装，可以通过调用 `encapsulate` 和 `decapsulate` 方法实现数据的传输。
- `Packet`：表示一个完整的数据包，包含应用层数据和各层的头部信息。
- `TransportHeader`：表示传输层头部（例如 TCP）。
- `NetworkHeade`r：表示网络层头部（例如 IPv4）。
- `DataLinkHeader`：表示数据链路层头部（例如以太网）。
- `NetworkSimulatorWriteTest`：测试类，用于封装数据并将其写入二进制文件 `encapsulatedData.bin`。
- `NetworkSimulatorReadTest`：测试类，用于从二进制文件中读取数据并进行解封装。
- `encapsulatedData.bin`：封装后的物理层数据文件。

### 使用

1. 封装数据并写入二进制文件：
   - 在 `NetworkSimulatorWriteTest` 类中，您可以修改应用层的数据内容（例如 `message` 变量）。
   - 运行 `NetworkSimulatorWriteTest` 类的 `main` 方法。
   - 运行后，程序将显示应用层数据和封装后的物理层数据，并将封装后的数据写入 `encapsulatedData.bin` 文件中。

2. 从二进制文件中读取数据并解封装
   - 确保 `encapsulatedData.bin` 文件存在于项目根目录。
   - 运行 `NetworkSimulatorReadTest` 类的 `main` 方法。
   - 程序将读取二进制文件中的物理层数据，进行解封装，并在控制台输出解封装后的应用层数据及各层头部信息。