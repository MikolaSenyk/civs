#!/bin/sh
#
mysql --user=civs --password=1234  < civs.sql
mysql --user=civs --password=1234  < civs_data.sql
