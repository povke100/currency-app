<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.TreeSet"%>
<%@ page import="java.util.List"%>
<%@ page import="lt.povilass.currencyapp.data.CurrencyRate"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Today's currency rates</title>
<style>
table, th, td {
	border: 1px solid black;
}

.calculator {
	padding: 5px;
}

#amount, #currency {
	width: 100px;
}

#calculate {
	
}

label, .result {
	width: 80px;
	text-align: right;
	display: inline-block;
	padding-right: 10px;
	padding-bottom: 5px;
}

.result {
	padding-bottom: 1px;
	height: 20px;
}

#resultL {
	width: auto;
}
</style>
</head>
<body>
	<div class="currency" style="display: inline-block;">
		<h3>Currency calculator</h3>
		<div class="calculator">
			<form action="/rates/calculator.do" method="get">
				Select the currency and enter the amount:
				<div class="amount" style="display: block;">
					<label>Amount:</label> <input type="number" value="0" step="0.0000001" name="amount" id=amount />
				</div>
				<div class="currency" style="display: block;">
					<label>Currency:</label>
					<%
						Set<String> codes = (TreeSet<String>) request.getAttribute("codes");
					%>
					<select name=currency id="currency">
						<%
							for (String code : codes) {
						%>
						<option value="<%=code%>"><%=code%></option>
						<%
							}
						%>
					</select>
				</div>
				<div class="result" style="display: inline;">
					<input type="submit" value="Calculate" id="calculate"> 
					<label id="resultL"><%String ans =(String) request.getAttribute("answer");%><%=ans%></label>
				</div>
			</form>
		</div>
	</div>
	<div class="rates">
		<h3>Current rates</h3>
		<table>
			<tr>
				<th>Date</th>
				<th>Currency</th>
				<th>Amount</th>
				<th>Change</th>
				<th>Change %</th>
			</tr>
			<%
				List<CurrencyRate> rates = (List<CurrencyRate>) request.getAttribute("rates");
				for (CurrencyRate rate : rates) {
					String date = rate.getDate();
					String curr = rate.getCurrency();
					String amount = rate.getAmount().stripTrailingZeros().toPlainString();
					String change = rate.getChange().stripTrailingZeros().toPlainString();
					String percent = rate.getPercentile().stripTrailingZeros().toPlainString();
			%>
			<tr>
				<td><%=date%></td>
				<td><a href="/rates/history.do?currcode=<%=curr%>"><%=curr%></a></td>
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