import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOImpl implements DAO {

	private Connection conn;
	
	public DAOImpl(){
		try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/store", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
    public void insertProduct (Product product) throws SQLException, DAOException{
        try{
            //PreparedStatement psmt = conn.prepareStatement("INSERT INTO dbo.Products (id, type, manufacturer, productionDate, expiryDate) VALUES (?,?,?,?,?);");
            PreparedStatement psmt = conn.prepareStatement("INSERT INTO `store`.`product` " +
                    "(`Product_ID`, `Type`, `Manufacturer`, `Production_Date`, `Expiry_Date`) VALUES (?, ?, ?, ?, ?);");
            psmt.setInt(1, product.getId());
            psmt.setString(2, product.getType());
            psmt.setString(3, product.getManufacturer());
            psmt.setString(4, product.getProductionDate());
            psmt.setString(5, product.getExpiryDate());
            psmt.executeUpdate();
        }
        catch (SQLException e) {
        	throw new DAOException(e);
        }
    }
    public Product getProduct(int ID) throws SQLException, DAOException
    {
    	Product product = null;
    	try
    	{
    		//conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-JA5ASAC;databaseName=Market", "sa", "passwd");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/store", "root", "");
    		PreparedStatement psmt = conn.prepareStatement("SELECT* FROM `store`.`product` WHERE `product`.`Product_ID` = ?");
    		psmt.setInt(1, ID);
            ResultSet res =  psmt.executeQuery();
            while (res.next()) {
            	product = new Product(res.getInt("Product_ID"));
            	product.setType(res.getString("Type"));
            	product.setManufacturer(res.getString("Manufacturer"));
            	product.setProductionDate(res.getString("Production_Date"));
            	product.setExpiryDate(res.getString("Expiry_Date"));
			}
    	}
    	catch (SQLException e) {
    		throw new DAOException(e);
		}
    	
    	return product;
    }
    public void updateProduct (Product product) throws SQLException, DAOException{
        try{
            PreparedStatement psmt = conn.prepareStatement("UPDATE `store`.`product` SET `Type` = ?, `Manufacturer` = ?, `Production_Date` = ?, `Expiry_Date` = ? WHERE `product`.`Product_ID` = ?;");
            psmt.setString(1, product.getType());
            psmt.setString(2, product.getManufacturer());
            psmt.setString(3, product.getProductionDate());
            psmt.setString(4, product.getExpiryDate());
            psmt.setInt(5, product.getId());
            psmt.executeUpdate();
        }
        catch (SQLException e) {
        	throw new DAOException(e);
        }
    }
    public void deleteProduct (int id) throws SQLException, DAOException{
        try{
            PreparedStatement psmt = conn.prepareStatement("DELETE FROM `store`.`product` WHERE `product`.`Product_ID` = ?");
            psmt.setInt(1, id);
            psmt.executeUpdate();
        }
        catch (SQLException e) {
        	throw new DAOException(e);
        }
    }
}
