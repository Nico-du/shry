package net.chinanets.service.imp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.tree.RowMapper;

import org.springframework.jdbc.core.JdbcTemplate;

import net.chinanets.service.StatisticService;
import net.chinanets.vo.StatisVO;

public class StatisticServiceImpl extends CommonServiceImp implements
		StatisticService {
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List statisEqual() {
		// TODO Auto-generated method stub
		String sql1="select '支出数' as label,sum(lfbs)-sum(ye) as data from Kmzc";
		String sql2="select '余额' as label,sum(ye) as data from Kmzc";
		List list1=jdbcTemplate.query(sql1, new StatisRowMapper());
		List list2=jdbcTemplate.query(sql2, new StatisRowMapper());
		list2.add(list1.get(0));
		return list2;
	}
	/**
	 * 计算原分配比率
	 */
	public List statisPre() {
		// TODO Auto-generated method stub
		String sql="select distinct xmmc as label,lfbs as data from Kmzc";
		return jdbcTemplate.query(sql, new StatisRowMapper());
	}
	/**
	 * 计算剩余率
	 */
	public List statisReal() {
		// TODO Auto-generated method stub
		String sql="select distinct xmmc as label,ye as data from Kmzc";
		return jdbcTemplate.query(sql, new StatisRowMapper());
	}
	
	class StatisRowMapper implements org.springframework.jdbc.core.RowMapper{

		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			StatisVO obj=new StatisVO();
			obj.setLabel(rs.getString("label"));
			obj.setData(rs.getString("data"));
			return obj;
		}
	}
}
