Sub SendDocumentToAPI()

    Dim excelApp As Object
    Dim workbook As Object
    Dim xmlFilePath As String

    ' 创建 Excel 应用程序实例
    Set excelApp = CreateObject("Excel.Application")

    ' 打开 Excel 文件
    Set workbook = excelApp.Workbooks.Open("#{[filePath]}")

    ' 保存 Excel 文件为 XML 格式
    xmlFilePath = "#{[xmlFilePath]}"
    workbook.SaveAs xmlFilePath, 46 ' 46 表示 xlXMLSpreadsheet 格式

    workbook.Close False
    excelApp.Quit

    ' 释放对象引用
    Set workbook = Nothing
    Set excelApp = Nothing
End Sub