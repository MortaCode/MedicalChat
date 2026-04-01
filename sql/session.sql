-- 会话表
CREATE TABLE `session` (
                                        `id` VARCHAR(32) NOT NULL COMMENT '会话ID，会话表主键',
                                        `user_id` VARCHAR(64) NOT NULL COMMENT '用户ID',
                                        `title` VARCHAR(200) DEFAULT NULL COMMENT '会话标题',
                                        `model_name` VARCHAR(50) NOT NULL COMMENT '使用的模型名称',
                                        `message_count` INT DEFAULT 0 COMMENT '消息总数',
                                        `del_flag` TINYINT DEFAULT 0 COMMENT '删除状态：0-未删除，1-删除',
                                        `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        PRIMARY KEY (`id`),
                                        KEY `idx_user_id` (`user_id`),
                                        KEY `idx_update_time` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对话会话表';

-- 对话消息表
CREATE TABLE `message` (
                                        `id` VARCHAR(32) NOT NULL COMMENT '消息ID，消息表主键',
                                        `session_id` VARCHAR(32) NOT NULL COMMENT '会话ID',
                                        `role` VARCHAR(20) NOT NULL COMMENT '角色：user/assistant/system',
                                        `content` TEXT NOT NULL COMMENT '消息内容',
                                        `message_type` VARCHAR(20) DEFAULT 'text' COMMENT '消息类型：text/image/file',
                                        `token_count` INT DEFAULT 0 COMMENT 'token数量',
                                        `del_flag` TINYINT DEFAULT 0 COMMENT '删除状态：0-未删除，1-删除',
                                        `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        PRIMARY KEY (`id`),
                                        KEY `idx_session_id` (`session_id`),
                                        KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对话消息表';

-- 会话记忆快照表（用于快速恢复）
CREATE TABLE `snapshot` (
                                         `id` VARCHAR(32) NOT NULL COMMENT '快照ID，快照表主键',
                                         `session_id` VARCHAR(32) NOT NULL COMMENT '会话ID',
                                         `snapshot_data` LONGBLOB NOT NULL COMMENT '序列化的记忆数据',
                                         `message_count` INT NOT NULL COMMENT '快照时的消息数量',
                                         `checksum` VARCHAR(64) DEFAULT NULL COMMENT '数据校验和',
                                         `del_flag` TINYINT DEFAULT 0 COMMENT '删除状态：0-未删除，1-删除',
                                         `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                         PRIMARY KEY (`id`),
                                         KEY `idx_session_id` (`session_id`),
                                         KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会话记忆快照表';