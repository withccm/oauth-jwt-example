CREATE TABLE `user` (
    `userNo` bigint(20) NOT NULL AUTO_INCREMENT,
    `oauthType` varchar(50) DEFAULT NULL COMMENT 'ProviderType\n구글, 페이스북 등등',
    `oauthId` varchar(2555) DEFAULT NULL,
    `name` varchar(255) DEFAULT NULL,
    `email` varchar(255) DEFAULT NULL,
    `imageUrl` varchar(255) DEFAULT NULL,
    `role` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`userNo`)
);

CREATE TABLE `userRefreshToken` (
    `refreshTokenSeq` bigint(20) NOT NULL AUTO_INCREMENT,
    `userNo` bigint(20) NOT NULL,
    `refreshToken` varchar(300) DEFAULT NULL,
    `refreshTokenExpires` datetime DEFAULT NULL,
    `accessToken` varchar(300) DEFAULT NULL,
    `accessTokenExpires` datetime DEFAULT NULL,
    `createdDate` datetime DEFAULT NULL,
    `updatedDate` datetime DEFAULT NULL,
    PRIMARY KEY (`refreshTokenSeq`),
    KEY `idx1` (`userNo`,`accessToken`),
    KEY `idx2` (`userNo`,`refreshToken`)
);

CREATE TABLE `book` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `bookname` varchar(200) DEFAULT NULL,
    PRIMARY KEY (`id`)
);