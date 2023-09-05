Sub SendDocumentToAPI()
    Dim doc As Document
    Dim byteData() As Byte
    Dim winHttp As Object

    ' 设置文档对象
    Set doc = ThisDocument

    ' 创建字节数组来存储文档内容
    ReDim byteData(1 To doc.Content.End - doc.Content.Start + 1)

    ' 将文档内容复制到字节数组
    doc.Content.Copy
    byteData = doc.Content.WordOpenXML ' 在Word中使用WordOpenXML属性

    ' 创建WinHTTP对象
    Set winHttp = CreateObject("WinHttp.WinHttpRequest.5.1")

    ' 配置WinHTTP请求
    winHttp.Open "POST", "http://192.168.226.128:8080/accept", False
    winHttp.setRequestHeader "Content-Type", "application/octet-stream"

    ' 发送二进制数据
    winHttp.send byteData

    ' 处理响应（如果需要）
    MsgBox winHttp.responseText

    ' 清理资源
    Set winHttp = Nothing
End Sub
