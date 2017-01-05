for ((i=1;i<=50;i++)); do   curl -v --header "Connection: keep-alive" "http://localhost:3333/accounts/123456789"; done
