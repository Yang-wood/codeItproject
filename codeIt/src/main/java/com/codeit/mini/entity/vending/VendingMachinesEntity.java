package com.codeit.mini.entity.vending;

import com.codeit.mini.entity.admin.Admin;
import com.codeit.mini.entity.comm.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@SequenceGenerator (
		name = "VM_ID_SEQ",
		sequenceName = "vm_id_seq",
		initialValue = 1,
		allocationSize = 1
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString (exclude = "adminId")
@Table (name = "vending_machine")
public class VendingMachinesEntity extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VM_ID_SEQ")
	private Long machineId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id", nullable = false, foreignKey = @ForeignKey(name="fk_vm_admin_id"))
	private Admin adminId;
	
	@Column(name = "name", length = 60, nullable = false)
	private String name;
	
	@Column(name = "type", length = 30, nullable = false)
	private String type;
	
	@Column(name = "description", length = 300)
	private String description;
	
//	1: 활성 / 0 : 비활성
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Integer isActive = 1;
    
    
    
    public void changeName(String newName) {
    	if (newName == null || newName.trim().isEmpty()) {
    		throw new IllegalArgumentException("자판기 이름은 비어 있을 수 없습니다.");
    	}
    	
    	if (newName.equals(name)) {
    		throw new IllegalArgumentException("기존 이름과 동일합니다.");
    	}
    	
    	this.name = newName;
    }
    
    public void changeDesc(String newDesc) {
    	if (newDesc.length() > 100) {
    		throw new IllegalArgumentException("자판기에 대한 설명은 100자 이내로 입력해주세요.");
    	}
    	
    	this.description = newDesc;
    }
    
    public void changeType(String newType) {
    	if (newType == null || newType.trim().isEmpty()) {
    		throw new IllegalArgumentException("타입을 지정해주세요.");
    	}
    	
    	if (newType.equals(type)) {
    		throw new IllegalArgumentException("기존 타입과 동일합니다.");
    	}
    }
    
    public void changeActive(Integer newActive) {
    	
    	this.isActive = newActive;
    }
    
    public void setAdminId(Admin adminId) {
        this.adminId = adminId;
    }

}
