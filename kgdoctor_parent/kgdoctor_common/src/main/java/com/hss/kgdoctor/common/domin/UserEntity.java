package com.hss.kgdoctor.common.domin;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("tb_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long userId;
	/**
	 * 
	 */
	private String userName;

	/**
	 * 
	 */
	private String email;
	/**
	 * 用户角色：0-普通用户，1-管理员，2-医生
	 */
	@TableField(fill = FieldFill.INSERT)
	private Integer role;
	/**
	 * 
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

}
