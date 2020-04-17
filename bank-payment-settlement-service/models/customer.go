package models

import (
	"fmt"
	"log"

	"github.com/astaxie/beego/orm"
)

type Customer struct {
	CustomerNumber int    `orm:"pk;column(customernumber)" json:"customernumber"`
	FirstName      string `orm:"column(firstname)" json:"firstName"`
	LastName       string `orm:"column(lastname)" json:"lastName"`
	AccountNumber  string `orm:"column(accountnumber)" json:"accountNumber"`
	Balance        int    `orm:"column(balance)" json:"balance"`
}

func init() {
	orm.RegisterModel(new(Customer))
}

func (u *User) TableName() string {
	return "customer"
}

func AddCustomer(c Customer) string {
	InsertCustomer(c)
	return ""
}

func InsertCustomer(cust Customer) {
	o := orm.NewOrm()
	// insert
	id, err := o.Insert(&cust)
	log.Printf("ID: %d, ERR: %v\n", id, err)
}

func GetCustomerByAccNo(custAccNo string) *Customer {
	o := orm.NewOrm()
	c := Customer{AccountNumber: custAccNo}
	log.Println(custAccNo)
	err := o.Read(&c, "accountnumber")
	fmt.Printf("Customer %s", c)
	fmt.Printf("ERR: %v\n", err)
	return &c
}

func GetCustomerByCustId(custId int) *Customer {
	o := orm.NewOrm()
	c := Customer{CustomerNumber: custId}
	err := o.Read(&c)
	fmt.Printf("Customer %s", c)
	fmt.Printf("ERR: %v\n", err)
	return &c
}

func UpdateCustomerBalance(cust *Customer) {
	o := orm.NewOrm()

	c := Customer{CustomerNumber: cust.CustomerNumber}
	if o.Read(&c) == nil {
		c.Balance = cust.Balance
		if num, err := o.Update(&c); err == nil {
			fmt.Println(num)
		}
	}
}
