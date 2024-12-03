# 计算机网络五层协议模拟

基本完成数据从应用层封装到物理层得到一个bin文件，然后可以将bin文件解封装到应用层获得封装的数据

## 功能

- `NetworkSimulator`:实现了五层协议的封装和解封装，可以通过调用sendData和receiveData方法实现数据的传输
- `Encapsulation`:实现了数据的封装
- `Decapsulation`:实现了数据的解封装
- `complex_physical_layer_data.bin`:封装后的数据

## 使用

1. 在Encapsulation类中修改数据，然后运行,可以得到封装后的数据`complex_physical_layer_data.bin`
2. 运行Decapsulation类，可以得到解封装后的数据，会在控制带输出封装的数据
