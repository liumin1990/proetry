<html>
<head>
    <title>唐诗检索系统</title>
    <meta charset="UTF-8">
</head>
<body>

<#list resultList as result>
    ${result.id} | ${result.name} | ${result.tilte} | ${result.content}
<hr>
</#list>

</body>
</html>