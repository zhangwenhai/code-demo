/*
 * 文件名: DatabaseInfo.java
 * 版    权：  Copyright   Tech. Co. Ltd. All Rights Reserved.
 * 描    述: 数据库保存基本信息
 * 创建人: zhangwenhai
 * 创建时间:2015-3-8
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
package com.android.component.database;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * 数据库存基本信息<BR>
 * @author zhangwenhai
 * @version [Client V20150307, 2015-3-8]
 */
public class DatabaseInfo
{
    /**
     * 定义数据库 xml 标签<BR>
     * @author zhangwenhai
     * @version [Client V20150307, 2015-3-8]
     */
    @Root(name = "database", strict = false)
    public static class Database
    {
        /**
         * 数据库的版本号
         */
        @Attribute(name = "version", required = true)
        private String version;
        
        /**
         * 数据库的名称
         */
        @Attribute(name = "name", required = true)
        private String name;
        
        /**
         * 数据库存放数据库的相对位置。目前暂时无定义，用于以后扩展
         */
        @Attribute(name = "dataPath", required = true)
        private String dataPath;
        
        /**
         * 数据库中的所有表
         */
        @ElementList(inline = true, entry = "table", required = false)
        private List<Table> listTable;
        
        public String getVersion()
        {
            return version;
        }
        
        public List<Table> getListTable()
        {
            return listTable;
        }
        
        public void setListTable(List<Table> listTable)
        {
            this.listTable = listTable;
        }
        
        public void setVersion(String version)
        {
            this.version = version;
        }
        
        public String getName()
        {
            return name;
        }
        
        public void setName(String name)
        {
            this.name = name;
        }
        
        public String getDataPath()
        {
            return dataPath;
        }
        
        public void setDataPath(String dataPath)
        {
            this.dataPath = dataPath;
        }
        
        /**
         * 用于定义数据库表结构<BR>
         * @author zhangwenhai
         * @version [Client V20150307, 2015-3-8]
         */
        @Root(name = "table", strict = false)
        public static class Table
        {
            
            /**
             * 数据库表名称
             */
            @Attribute(name = "name", required = true)
            private String name;
            
            @ElementList(inline = true, entry = "field", required = false)
            private List<Field> listFiled;
            
            public String getName()
            {
                return name;
            }
            
            public void setName(String name)
            {
                this.name = name;
            }
            
            public List<Field> getListFiled()
            {
                return listFiled;
            }
            
            public void setListFiled(List<Field> listFiled)
            {
                this.listFiled = listFiled;
            }
        }
        
        /**
         * 用于描述数据库表结构中的字段<BR>
         * @author zhangwenhai
         * @version [Client V20150307, 2015-3-8]
         */
        @Root(name = "field", strict = false)
        public static class Field
        {
            /**
             * 字段名称
             */
            @Attribute(name = "name", required = true)
            private String name;
            
            /**
             * 字段类型。Sqlite3支持的数据库字段类型。可为：text,integer,real,blob
             */
            @Attribute(name = "type", required = true)
            private String type;
            
            /**
             * 描述字段的属性约束。约束为可为下列值的组合: ”primary key”,” autoincrement”,” not
             * null”,” unique”,” default”。 这些字段的含义如下： primary key为主键；
             * autoincrement为自增长； not null为非空； unique为唯一；
             * default为默认值。default后需跟默认的值，但不能有CURRENT_TIME, CURRENT_DATE
             * 或CURRENT_TIMESTAMP等默认值。 若定义了NOT NULL约束，则字段必须有一个非空的缺省值。
             */
            @Attribute(name = "obligatory", required = true)
            private String obligatory;
            
            public String getName()
            {
                return name;
            }
            
            public void setName(String name)
            {
                this.name = name;
            }
            
            public String getType()
            {
                return type;
            }
            
            public void setType(String type)
            {
                this.type = type;
            }
            
            public String getObligatory()
            {
                return obligatory;
            }
            
            public void setObligatory(String obligatory)
            {
                this.obligatory = obligatory;
            }
        }
    }
    
    /**
     * 全局信息表<BR>
     * @author zhangwenhai
     * @version [Client V20150307, 2015-3-8]
     */
    public interface Tables
    {
        /**
         * 全局数据库版本号表
         */
        public static final String GLOBALDBVER = "dbVer";
    }
    
    /**
     * 数据库版本号表 列名字段描述<BR>
     * @author zhangwenhai
     * @version [Client V20150307, 2015-3-8]
     */
    public interface GlobalDbVerColumn
    {
        /**
         * 唯一标识
         */
        public static final String ID = "_ID";
        
        /**
         * 数据库的版本信息
         */
        public static final String GLOBAL_DB_VER = "dbVer";
        
        /**
         * 数据库表结构的描述信息
         */
        public static final String DESC = "desc";
    }
    
}
