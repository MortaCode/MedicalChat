CREATE TABLE T_TZGL_LTJLB (
                              id VARCHAR2(32) NOT NULL,
                              role VARCHAR2(20) NOT NULL,
                              content CLOB NOT NULL,
                              del_flag NUMBER(1) DEFAULT 0 NOT NULL,
                              create_by VARCHAR2(50),
                              update_by VARCHAR2(50),
                              create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                              CONSTRAINT PK_T_TZGL_LTJLB PRIMARY KEY (id)
);

COMMENT ON TABLE T_TZGL_LTJLB IS '会话表';
COMMENT ON COLUMN T_TZGL_LTJLB.id IS '会话ID，主键';
COMMENT ON COLUMN T_TZGL_LTJLB.role IS '角色：user/assistant/system';
COMMENT ON COLUMN T_TZGL_LTJLB.content IS '会话内容';
COMMENT ON COLUMN T_TZGL_LTJLB.del_flag IS '删除标志：0-未删除，1-已删除';
COMMENT ON COLUMN T_TZGL_LTJLB.create_by IS '创建人';
COMMENT ON COLUMN T_TZGL_LTJLB.update_by IS '更新人';
COMMENT ON COLUMN T_TZGL_LTJLB.create_time IS '创建时间';
COMMENT ON COLUMN T_TZGL_LTJLB.update_time IS '更新时间';

CREATE OR REPLACE TRIGGER TRG_T_TZGL_LTJLB_UPDATE
BEFORE UPDATE ON T_TZGL_LTJLB
FOR EACH ROW
BEGIN
    :NEW.update_time := CURRENT_TIMESTAMP;
END;