package main

import (
	"fmt"
	"log"

	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	"github.com/hackranger/bank-payment-settlement-service/pubsub"
	_ "github.com/hackranger/bank-payment-settlement-service/routers"
	_ "github.com/lib/pq"
)

func init() {

	host := "db-postgresql-blr1-12547-do-user-7354733-0.a.db.ondigitalocean.com"
	port := 25060
	user := "doadmin"
	password := "c2odvi1n97c0d7ph"
	dbname := "simpledatabase"

	psqlInfo := fmt.Sprintf("postgres://%s:%s@%s:%d/%s?"+
		"sslmode=require", user, password, host, port, dbname)

	err := orm.RegisterDriver("postgres", orm.DRPostgres)
	if err != nil {
		log.Println(err)
	}
	// set default database
	err = orm.RegisterDataBase("default", "postgres", psqlInfo, 30)
	if err != nil {
		log.Println(err)
	}
	name := "default"
	force := false
	verbose := false
	err = orm.RunSyncdb(name, force, verbose)
	if err != nil {
		fmt.Println(err)
	}
}

func main() {
	log.SetFlags(log.LstdFlags | log.Lshortfile)
	if beego.BConfig.RunMode == "dev" {
		beego.BConfig.WebConfig.DirectoryIndex = true
		beego.BConfig.WebConfig.StaticDir["/swagger"] = "swagger"
	}

	go pubsub.FundTransferSub()
	go pubsub.NewCustomerSub()

	beego.Run()
}
