<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/include.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Featuredata 상세 리스트</title>

<style type="text/css">
.tablescroll {
    width: 100%;
    height: 300px;
    overflow: auto;
}
</style>
</head>
<body>
	<div class="tablescroll" >
		<c:forEach items="${list}" var="data">
			<table style="border-collapse: collapse; width: 100%; "id="feature_detail">
				<thead>
					<c:forEach items="${data.attributes}" var="attr">
						<c:choose>
							<c:when test="${attr.name eq 'objectName'}">
								<th colspan="3" id="detail_idx" class="detal_num"> ${attr.value}</th>
							</c:when>
	        			 </c:choose>
					</c:forEach> 
				</thead>
				<thead style="display: none;">
					<th colspan="3" id="detail_idx" class="detal_num">${data.idx}</th>
				</thead>
				<thead>
					<th colspan="3">${data.name}</th>
					<tr>
						<th style="display: none">id</th>
						<th>name</th>
						<th>value</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${data.attributes}" var="attr">
						<tr>
							<td style="display: none;">${attr.a_idx}</td>
							<td style="width: 141px">${attr.name}</td>
							<td>${attr.value}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:forEach>
	</div>
</body>
<script type="text/javascript">
</script>
</html>