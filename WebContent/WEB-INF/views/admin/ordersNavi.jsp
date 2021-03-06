
<%
	response.addHeader("Refresh", "300");
%>
<div class="row-fluid">
	<div class="span12">
		<ul class="nav nav-tabs">
			<c:choose>
				<c:when test="${orderPage == 'new'}">
					<li class="active"><a href="new">Uudet <span
							class="badge badge-info"><c:out
									value="${ordersCount.newOrdersCount}" default="" /></span></a></li>
				</c:when>
				<c:otherwise>
					<li><a href="new">Uudet <span class="badge badge-info"><c:out
									value="${ordersCount.newOrdersCount}" default="" /></span></a></li>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${orderPage == 'accepted'}">
					<li class="active"><a href="accepted">Kuitatut <span
							class="badge badge-info"><c:out
									value="${ordersCount.acceptedOrdersCount}" default="" /></span></a></li>
				</c:when>
				<c:otherwise>
					<li><a href="accepted">Kuitatut <span
							class="badge badge-info"><c:out
									value="${ordersCount.acceptedOrdersCount}" default="" /></span></a></li>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${orderPage == 'taken'}">
					<li class="active"><a href="taken">Toimitetut <span
							class="badge badge-info"><c:out
									value="${ordersCount.takenOrdersCount}" default="" /></span></a></li>
				</c:when>
				<c:otherwise>
					<li><a href="taken">Toimitetut <span
							class="badge badge-info"><c:out
									value="${ordersCount.takenOrdersCount}" default="" /></span></a></li>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${orderPage == 'completed'}">
					<li class="active"><a href="completed">Valmiit <span
							class="badge badge-info"><c:out
									value="${ordersCount.completedOrdersCount}" default="" /></span></a></li>
				</c:when>
				<c:otherwise>
					<li><a href="completed">Valmiit <span
							class="badge badge-info"><c:out
									value="${ordersCount.completedOrdersCount}" default="" /></span></a></li>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${orderPage == 'billed'}">
					<li class="active"><a href="billed">Laskutetut</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="billed">Laskutetut</a></li>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${orderPage == 'searchOrdersPage'}">
					<li class="active"><a href="searchOrders">Hae tilauksia</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="searchOrders">Hae tilauksia</a></li>
				</c:otherwise>
			</c:choose>

			<c:choose>
				<c:when test="${orderPage == 'deletedOrders'}">
					<li class="active"><a href="deletedOrders">Poistetut</a></li>
				</c:when>
				<c:otherwise>
					<li><a href="deletedOrders">Poistetut</a></li>
				</c:otherwise>
			</c:choose>
			<c:if test="${orderPage != 'searchOrdersPage'}">
			<li>
			<form class="navbar-form" action="backupOrders" method="post">
			<input type="hidden" name="orderPage" id="orderPage" value="${orderPage}" />
				<button type="submit" class="btn">Varmuuskopioi</button>
			</form>
			</li>
			</c:if>
		</ul>
	</div>
</div>