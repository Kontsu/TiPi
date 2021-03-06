package tipi.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import tipi.bean.OrderForm;
import tipi.bean.OrderFormImpl;
import tipi.bean.OrdersCount;
import tipi.bean.OrdersCountImpl;

/**
 * 
 * @author Lauri Soivi, Joona Viertola, Samuel Kontiomaa
 * @version 1.0
 * @since 18.12.2013
 * DAO saves, updates and gets orders
 */

@Repository
public class OrdersDAOImpl implements OrdersDAO {

	@Inject
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Method gets all orders from DAO by statusOfOrder
	 */
	@Override
	public List<OrderForm> getOrderList(int statusOfOrder) {
		String sql = "SELECT * FROM orders WHERE statusOfOrder = ?;";
		Object[] data = new Object[] { statusOfOrder };
		List<OrderForm> resultList = getJdbcTemplate().query(sql,
				new BeanPropertyRowMapper(OrderFormImpl.class), data);

		return resultList;
	}

	/**
	 * Method searches one order with id from database
	 */
	@Override
	public OrderForm getOrderBean(int id) {
		String sql = "SELECT * FROM orders WHERE orders_id = ?;";
		Object[] data = new Object[] { id };
		OrderForm resultObject = getJdbcTemplate().queryForObject(sql, data,
				new BeanPropertyRowMapper(OrderFormImpl.class));

		return resultObject;
	}

	/**
	 * Method updates orders status
	 */
	@Override
	public void updateOrderStatusDAO(int orders_id, int statusOfOrder) {
		String newPasswordQuery = "UPDATE orders SET statusOfOrder=? WHERE orders_id=?;";
		jdbcTemplate.update(newPasswordQuery, new Object[] { statusOfOrder,
				orders_id });
	}

	/**
	 * Method gets orderlist for user with criteria what user wants
	 */
	@Override
	public List<OrderForm> getOrderListForUser(int user_id, int hasNewDestination, int companyMadeOrder, int statusOfOrder) {
		
		boolean hasNewDestination1 = true;
		boolean hasNewDestination2 = false;
		String user_idString = Integer.toString(user_id);
		String companyMadeOrderString = Integer.toString(companyMadeOrder);
		String statusOfOrderString = Integer.toString(statusOfOrder);
		
		if (hasNewDestination == 1)
			hasNewDestination1 = false;
		else if (hasNewDestination == 2)
			hasNewDestination2 = true;
		if(companyMadeOrder != 0)
			user_idString = "%";
		else
			companyMadeOrderString = "%";
		if(statusOfOrder == 0)
			statusOfOrderString = "%";
		
		String sql = "SELECT * FROM orders WHERE userMadeOrder LIKE ? AND (hasNewDestination = ? OR hasNewDestination = ?) AND companyMadeOrder LIKE ? AND (statusOfOrder LIKE ? AND statusOfOrder NOT LIKE 6 AND statusOfOrder NOT LIKE 5);";
		Object[] data = new Object[] { user_idString, hasNewDestination1, hasNewDestination2, companyMadeOrderString, statusOfOrderString };
		List<OrderForm> resultList = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper(OrderFormImpl.class), data);

		return resultList;
	}

	/**
	 * Method gets count of orders from database: new, accepted, taken, completed
	 */
	@Override
	public OrdersCount ordersCountDAO(OrdersCount ordersCount) {
		String sql = "SELECT COUNT(CASE WHEN statusOfOrder =1 THEN 1 end) as newOrdersCount,"
				+ " COUNT(CASE WHEN statusOfOrder =2 THEN 1 end) as acceptedOrdersCount,"
				+ " COUNT(CASE WHEN statusOfOrder =3 THEN 1 end) as takenOrdersCount,"
				+ " COUNT(CASE WHEN statusOfOrder =4 THEN 1 end) as completedOrdersCount FROM orders;";
		ordersCount = (OrdersCount) jdbcTemplate.queryForObject(sql,
				new BeanPropertyRowMapper(OrdersCountImpl.class));
		return ordersCount;
	}

	/**
	 * Method gets dates and times of order. Used to compare times to current in controller.
	 */
	@Override
	public Map<String, Object> getOrdeDatesAndTimes(int id) {
		String sql = "SELECT collectionDate, collectionTime, destinationDate, destinationTime, nextDestinationCollectionDate, nextDestinationCollectionTime, nextDestinationDate, nextDestinationTime FROM orders WHERE orders_id = ?;";
		Object[] data = new Object[] { id };
		Map<String, Object> resultMap = jdbcTemplate.queryForMap(sql, data);

		return resultMap;
	}

	/**
	 * Method searches orders to admin. If admin some values are null method changes values to wildcards
	 */
	@Override
	public List<OrderForm> searchOrdersFromDAO(OrderForm searchOrders) {
		String carBrand = "%";
		String carModel = "%";
		String carRegister = "%";
		String collectionCity = "%";
		String destinationCity = "%";
		String nextDestinationCity = "%";
		boolean hasNewDestination = true;
		boolean hasNewDestination2 = false;
		String companyMadeOrder = "%";

		if (!searchOrders.getCarBrand().isEmpty())
			carBrand = searchOrders.getCarBrand();
		if (!searchOrders.getCarModel().isEmpty())
			carModel = searchOrders.getCarModel();
		if (!searchOrders.getCarRegister().isEmpty())
			carRegister = searchOrders.getCarRegister();
		if (!searchOrders.getCollectionCity().isEmpty())
			collectionCity = searchOrders.getCollectionCity();
		if (!searchOrders.getDestinationCity().isEmpty())
			destinationCity = searchOrders.getDestinationCity();
		if (!searchOrders.getNextDestinationCity().isEmpty())
			nextDestinationCity = searchOrders.getNextDestinationCity();
		if (searchOrders.getHasNewDestinationForSearchOrders() == 1)
			hasNewDestination = false;
		else if (searchOrders.getHasNewDestinationForSearchOrders() == 2)
			hasNewDestination2 = true;
		if(searchOrders.getCompanyMadeOrder() != 0)
			companyMadeOrder = Integer.toString(searchOrders.getCompanyMadeOrder());

		String sql = "SELECT * FROM orders WHERE carBrand LIKE ? AND carModel LIKE ? AND carRegister LIKE ?"
				+ " AND collectionCity LIKE ? AND destinationCity LIKE ? AND (nextDestinationCity LIKE ? OR nextDestinationCity IS NULL)"
				+ "AND (hasNewDestination = ? OR hasNewDestination = ?) AND companyMadeOrder LIKE ?;";
		Object[] data = new Object[] { 
				carBrand, carModel,
				carRegister, collectionCity,
				destinationCity, nextDestinationCity, hasNewDestination, hasNewDestination2,
				companyMadeOrder				
				};
		List<OrderForm> orders = getJdbcTemplate().query(sql,
				new BeanPropertyRowMapper(OrderFormImpl.class), data);
		return orders;
	}
	
	/**
	 * Method changes order status to deleted. Not really deleting the order from dao
	 */
	@Override
	public void deleteOrder(int orderId) {
		String sql = "UPDATE orders SET lastTimeEdited = now(), statusOfOrder = 6 WHERE orders_id = ?";
		Object[] data =  new Object[] { orderId };
		getJdbcTemplate().update(sql, data);
	}
	
	/**
	 * Method updates modified order.
	 */
	@Override
	public boolean updateOrderByUser(OrderForm order, boolean collectionTimeLimit, boolean nextDestinationTimeLimit) {
		
		SimpleDateFormat oldFormat = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

		if (collectionTimeLimit || nextDestinationTimeLimit) {
			Object[] data;
			ArrayList<Object> cacheArray = new ArrayList<Object>();
			String sql = "UPDATE orders SET lastTimeEdited = now(), companyMadeOrder = ?, userMadeOrder = ?, ";
			cacheArray.add(order.getCompanyMadeOrder());
			cacheArray.add(order.getUserMadeOrder());
			
			if (collectionTimeLimit) {
				try  {
					System.out.println("collectionTimeLimit = true");
					sql = sql + "carBrand = ?, carRegister = ?, carModel = ?, carColor = ?, collectionDate = ?, destinationDate = ?, collectionTime = ?, destinationTime = ?, collectionAddress = ?, destinationAddress = ?, collectionPostalCode = ?, destinationPostalCode = ?, collectionCity = ?, destinationCity = ?, clientFname = ?, clientPhoneNo = ?, clientLname = ?, clientCompany = ?, additionalInformation = ?";
					cacheArray.add(order.getCarBrand());
					cacheArray.add(order.getCarRegister());
					cacheArray.add(order.getCarModel());
					cacheArray.add(order.getCarColor());
					cacheArray.add(newFormat.format(oldFormat.parse(order.getCollectionDate())));
					cacheArray.add(newFormat.format(oldFormat.parse(order.getDestinationDate())));
					cacheArray.add(order.getCollectionTime());
					cacheArray.add(order.getDestinationTime());
					cacheArray.add(order.getCollectionAddress());
					cacheArray.add(order.getDestinationAddress());
					cacheArray.add(order.getCollectionPostalCode());
					cacheArray.add(order.getDestinationPostalCode());
					cacheArray.add(order.getCollectionCity());
					cacheArray.add(order.getDestinationCity());
					cacheArray.add(order.getClientFname());
					cacheArray.add(order.getClientPhoneNo());
					cacheArray.add(order.getClientLname());
					cacheArray.add(order.getClientCompany());
					cacheArray.add(order.getAdditionalInformation());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			if (order.isHasNewDestination()) {
				if (nextDestinationTimeLimit) {
					try {
						System.out.println("nextDestinationTimeLimit = true");
						if (collectionTimeLimit) {sql = sql + ", ";}
						sql = sql + "nextDestinationCollectionDate = ?, nextDestinationDate = ?, nextDestinationCollectionTime = ?, nextDestinationTime = ?, nextDestinationAddress = ?, nextDestinationPostalCode = ?, nextDestinationCity = ?, nextAdditionalInformation = ?";
						cacheArray.add(newFormat.format(oldFormat.parse(order.getNextDestinationCollectionDate())));
						cacheArray.add(newFormat.format(oldFormat.parse(order.getNextDestinationDate())));
						cacheArray.add(order.getNextDestinationCollectionTime());
						cacheArray.add(order.getNextDestinationTime());
						cacheArray.add(order.getNextDestinationAddress());
						cacheArray.add(order.getNextDestinationPostalCode());
						cacheArray.add(order.getNextDestinationCity());
						cacheArray.add(order.getNextAdditionalInformation());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			
			cacheArray.add(order.getOrders_id());
			sql = sql + " WHERE orders_id = ?";
			data = cacheArray.toArray();
			
			int update = getJdbcTemplate().update(sql, data);
			
			if (update == 1) {
				return true;
			} else { 
				return false;
			}
		}
		
		return false;
	}
	
}
