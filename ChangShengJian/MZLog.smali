.class public Lcom/mzheng/MZLog;
.super Ljava/lang/Object;
.source "MZLog.java"


# direct methods
.method public constructor <init>()V
    .registers 1

    .prologue
    .line 7
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method

.method public static Log(Ljava/lang/Object;)V
    .registers 3
    .param p0, "someObj"    # Ljava/lang/Object;

    .prologue
    .line 16
    const-string v0, "mzheng"

    invoke-virtual {p0}, Ljava/lang/Object;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v1}, Lcom/mzheng/MZLog;->Log(Ljava/lang/String;Ljava/lang/String;)V

    .line 17
    return-void
.end method

.method public static Log(Ljava/lang/String;Ljava/lang/String;)V
    .registers 2
    .param p0, "tag"    # Ljava/lang/String;
    .param p1, "msg"    # Ljava/lang/String;

    .prologue
    .line 11
    invoke-static {p0, p1}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 12
    return-void
.end method

.method public static Log([Ljava/lang/Object;)V
    .registers 3
    .param p0, "someObj"    # [Ljava/lang/Object;

    .prologue
    .line 21
    const-string v0, "mzheng"

    invoke-static {p0}, Ljava/util/Arrays;->toString([Ljava/lang/Object;)Ljava/lang/String;

    move-result-object v1

    invoke-static {v0, v1}, Lcom/mzheng/MZLog;->Log(Ljava/lang/String;Ljava/lang/String;)V

    .line 22
    return-void
.end method
