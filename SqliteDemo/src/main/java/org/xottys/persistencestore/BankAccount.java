package org.xottys.persistencestore;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by changqing on 2017/7/6.
 */
@Entity
public class BankAccount {
    @Id
    private Long id;
    private String name;
    private String accountNumber;
    private int balance;

    @Transient
    private int age; // not persisted
    @Generated(hash = 1168028655)
    public BankAccount(Long id, String name, String accountNumber, int balance) {
        this.id = id;
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
    @Generated(hash = 156644868)
    public BankAccount() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getBalance() {
        return this.balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }
    public String getAccountNumber() {
        return this.accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
