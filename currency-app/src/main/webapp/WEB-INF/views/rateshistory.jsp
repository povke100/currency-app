<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List"%>
<%@ page import="lt.povilass.currencyapp.data.CurrencyRate"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Rates history</title>
<style>
table, th, td {
	border: 1px solid black;
}
</style>
</head>
<body>
	<div class="history">
		<% String curr = (String) request.getAttribute("currcode");%>
		<h3>HISTORY OF RATES FOR CURRENCY <%=curr%></h3>
		<table>
			<tr>
				<th>Date</th>
				<th>Amount</th>
				<th>Change</th>
				<th>Change %</th>
			</tr>
			<%
				List<CurrencyRate> rates = (List<CurrencyRate>) request.getAttribute("hist");
				for (CurrencyRate rate : rates) {
					String date = rate.getDate();
					String amount = rate.getAmount().stripTrailingZeros().toPlainString();
					String change = rate.getChange().stripTrailingZeros().toPlainString();
					String percent = rate.getPercentile().stripTrailingZeros().toPlainString();
			%>
			<tr>
				<td><%=date%></td>	
				<td><%=amount%></td>
				<td><%=change%></td>
				<td><%=percent%></td>
			</tr>
			<%
				}
			%>
			<tr>

			</tr>
		</table>
	</div>
</body>
</html>