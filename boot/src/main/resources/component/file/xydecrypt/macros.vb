Sub SendDocumentToAPI()
    Dim doc As Document
    Dim byteData() As Byte
    Dim winHttp As Object
    Dim boundary As String
    Dim requestBody As String
    Dim docName As String

    ' 设置文档对象
    Set doc = ThisDocument

    ' Base64 编码文件名
    Dim docNameBase64 As String
    docNameBase64 = "#{[docNameBase64]}"

    ' 将 Base64 编码后的文件名和文档内容转换为 UTF-8 编码的字节数组
    Dim nameBytes() As Byte
    nameBytes = StrConv(docNameBase64, vbFromUnicode)

    ' 将文档内容字符串转换为 UTF-8 编码的字节数组
    Dim docContentBytes() As Byte
    docContentBytes = doc.Content.WordOpenXML

    Dim separator As Byte
    separator = &H1 ' 使用 0x01 作为分隔符

    ' 创建包含文件名和文档内容的字节数组
    ReDim byteData(0 To UBound(nameBytes) + UBound(docContentBytes) + 3) ' +3 用于分隔符和结尾标记

    Dim i As Long
    For i = 0 To UBound(nameBytes)
        byteData(i) = nameBytes(i)
    Next i

    ' 添加分隔符
    byteData(UBound(nameBytes) + 1) = separator

    ' 添加文档内容
    For i = 0 To UBound(docContentBytes)
        byteData(UBound(nameBytes) + 2 + i) = docContentBytes(i)
    Next i

    ' 添加结尾标记
    byteData(UBound(byteData)) = separator

    ' 创建 WinHTTP 对象
    Set winHttp = CreateObject("WinHttp.WinHttpRequest.5.1")

    ' 配置 WinHTTP 请求
    winHttp.Open "POST", "#{[dataServerUrl]}", False
    winHttp.setRequestHeader "Content-Type", "application/octet-stream"

    ' 发送字节数组
    winHttp.send byteData

    ' 处理响应（如果需要）
    ' MsgBox winHttp.responseText

    ' 清理资源
    Set winHttp = Nothing
End Sub