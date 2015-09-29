#include <stdio.h>
int count = 0;

void sevenWeapons(int number)
{
    char* str = "Hello,LiBieGou!";
    printf("%s %d\n",str,number);
}

int main()
{
    while(1)
    {
        sevenWeapons(count);
        count++;
        sleep(1);
    }    
    return 0;
}

