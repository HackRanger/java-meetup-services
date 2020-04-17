package models

type FundTransferInstruction struct {
	SourceAccountNumber      string `json:"srcAccountNumber"`
	DestinationAccountNumber string `json:"destAccountNumber"`
	Amount                   int    `json:"amount"`
}
