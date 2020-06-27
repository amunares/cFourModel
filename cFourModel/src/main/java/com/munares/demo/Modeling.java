package com.munares.demo;

import java.security.GeneralSecurityException;
import java.util.stream.Collectors;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.Component;
import com.structurizr.model.Container;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.model.Tags;
import com.structurizr.view.ComponentView;
import com.structurizr.view.ContainerView;
import com.structurizr.view.PaperSize;
import com.structurizr.view.Shape;
import com.structurizr.view.Styles;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;

public class Modeling {
   
    public static void main(final String[] args) throws Exception {

/*        System.out.println("Java corriendo");
        Banking();
      

    }
    static void Banking(){*/
     
        final long workspaceId = 55972;
        final String apiKey = "a217ad25-4b84-4c25-80a5-cb8f3b00684c";
        final String apiSecret = "dd7ea9d2-7e72-4c2e-b9e0-f262811a1eae";

        StructurizrClient structurizrClient = new StructurizrClient(apiKey, apiSecret);
        
        Workspace workspace = new Workspace("Banking", "Banking - C4 Model");
        Model model = workspace.getModel();

            SoftwareSystem internetBankingSystem = model.addSoftwareSystem("Internet Banking", "Permite a los clientes consultar información de sus cuentas y realizar operaciones.");
            SoftwareSystem mainframeBankingSystem = model.addSoftwareSystem("Mainframe Banking", "Almacena información del core bancario.");
            SoftwareSystem mobileAppSystem = model.addSoftwareSystem("Mobile App", "Permite a los clientes consultar información de sus cuentas y realizar operaciones.");
            SoftwareSystem emailSystem = model.addSoftwareSystem("SendGrid", "Servicio de envío de notificaciones por email.");

            Person cliente = model.addPerson("Cliente", "Cliente del banco.");
            Person cajero = model.addPerson("Cajero", "Empleado del banco.");

            mainframeBankingSystem.addTags("Mainframe");
            mobileAppSystem.addTags("Mobile App");
            emailSystem.addTags("SendGrid");

            cliente.uses(internetBankingSystem, "Realiza consultas y operaciones bancarias.");
            cliente.uses(mobileAppSystem, "Realiza consultas y operaciones bancarias.");
            cajero.uses(mainframeBankingSystem, "Usa");

            internetBankingSystem.uses(mainframeBankingSystem, "Usa");
            internetBankingSystem.uses(emailSystem, "Envía notificaciones de email");
            mobileAppSystem.uses(internetBankingSystem, "Usa");

            emailSystem.delivers(cliente, "Envía notificaciones de email", "SendGrid");

            ViewSet viewSet = workspace.getViews();

            // 1. Diagrama de Contexto
            SystemContextView contextView = viewSet.createSystemContextView(internetBankingSystem, "Contexto", "Diagrama de contexto - Banking");
            contextView.setPaperSize(PaperSize.A4_Landscape); 
//            contextView.PaperSize = PaperSize.A4_Landscape;
            contextView.addAllSoftwareSystems();
            contextView.addAllPeople();
            //contextView.EnableAutomaticLayout();

            Styles styles = viewSet.getConfiguration().getStyles();
            styles.addElementStyle(Tags.PERSON).background("#0a60ff").color("#ffffff").shape(Shape.Person);
            styles.addElementStyle("Mobile App").background("#29c732").color("#ffffff").shape(Shape.MobileDevicePortrait);
            styles.addElementStyle("Mainframe").background("#90714c").color("#ffffff").shape(Shape.RoundedBox);
            styles.addElementStyle("SendGrid").background("#a5cdff").color("#ffffff").shape(Shape.RoundedBox);

            // 2. Diagrama de Contenedores
            Container webApplication = internetBankingSystem.addContainer("Aplicación Web", "Permite a los clientes consultar información de sus cuentas y realizar operaciones.", "ReactJS, nginx port 80");
            Container restApi = internetBankingSystem.addContainer("RESTful API", "Permite a los clientes consultar información de sus cuentas y realizar operaciones.", "Java, nginx port 80");
            Container worker = internetBankingSystem.addContainer("Worker", "Manejador del bus de mensajes.", "Java");
            Container database = internetBankingSystem.addContainer("Base de Datos", "Repositorio de información bancaria.", "Oracle 12c port 1521");
            Container messageBus = internetBankingSystem.addContainer("Bus de Mensajes", "Transporte de eventos del dominio.", "RabbitMQ");

            webApplication.addTags("WebApp");
            restApi.addTags("API");
            worker.addTags("Worker");
            database.addTags("Database");
            messageBus.addTags("MessageBus");

            cliente.uses(webApplication, "Usa", "https 443");
            webApplication.uses(restApi, "Usa", "https 443");
            worker.uses(restApi, "Usa", "https 443");
            worker.uses(messageBus, "Usa");
            worker.uses(mainframeBankingSystem, "Usa");
            restApi.uses(database, "Usa", "jdbc 1521");
            restApi.uses(messageBus, "Usa");
            restApi.uses(emailSystem, "Usa", "https 443");
            mobileAppSystem.uses(restApi, "Usa");

            styles.addElementStyle("WebApp").background("#9d33d6").color("#ffffff").shape(Shape.WebBrowser).icon("data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9Ii0xMS41IC0xMC4yMzE3NCAyMyAyMC40NjM0OCI+CiAgPHRpdGxlPlJlYWN0IExvZ288L3RpdGxlPgogIDxjaXJjbGUgY3g9IjAiIGN5PSIwIiByPSIyLjA1IiBmaWxsPSIjNjFkYWZiIi8+CiAgPGcgc3Ryb2tlPSIjNjFkYWZiIiBzdHJva2Utd2lkdGg9IjEiIGZpbGw9Im5vbmUiPgogICAgPGVsbGlwc2Ugcng9IjExIiByeT0iNC4yIi8+CiAgICA8ZWxsaXBzZSByeD0iMTEiIHJ5PSI0LjIiIHRyYW5zZm9ybT0icm90YXRlKDYwKSIvPgogICAgPGVsbGlwc2Ugcng9IjExIiByeT0iNC4yIiB0cmFuc2Zvcm09InJvdGF0ZSgxMjApIi8+CiAgPC9nPgo8L3N2Zz4K");
            styles.addElementStyle("API").background("#929000").color("#ffffff").shape(Shape.RoundedBox).icon("https://image.flaticon.com/icons/svg/919/919854.svg");
          //  styles.addElementStyle("API").background("#929000").color("#ffffff").shape(Shape.RoundedBox).icon("https://www.google.com/search?q=la+imagen+java&rlz=1C1NHXL_esPE813PE814&sxsrf=ALeKk03p0K5cmTvMuITd7plEKwEIvmCnLg:1593233579288&source=lnms&tbm=isch&sa=X&ved=2ahUKEwj2iqbMmaHqAhUELLkGHQx9BZoQ_AUoAnoECAsQBA&biw=1920&bih=920#imgrc=ClMA3U7jQHAFHM");
            styles.addElementStyle("Worker").icon("https://image.flaticon.com/icons/svg/226/226777.svg");
            styles.addElementStyle("Database").background("#ff0000").color("#ffffff").shape(Shape.Cylinder).icon("https://4.bp.blogspot.com/-5JVtZBLlouA/V2LhWdrafHI/AAAAAAAADeU/_3bo_QH1WGApGAl-U8RkrFzHjdH6ryMoQCLcB/s200/12cdb.png");
            styles.addElementStyle("MessageBus").width(850).background("#fd8208").color("#ffffff").shape(Shape.Pipe).icon("https://www.rabbitmq.com/img/RabbitMQ-logo.svg");

            ContainerView containerView = viewSet.createContainerView(internetBankingSystem, "Contenedor", "Diagrama de contenedores - Banking");
            contextView.setPaperSize(PaperSize.A4_Landscape); 
            containerView.addAllElements();

            //containerView.EnableAutomaticLayout();
            // 3. Diagrama de Componentes
            Component transactionController = restApi.addComponent("Transactions Controller", "Allows users to perform transactions.", "Java REST Controller");
            Component signinController = restApi.addComponent("SignIn Controller", "Allows users to sign in to the Internet Banking System.", "Java REST Controller");
            Component accountsSummaryController = restApi.addComponent("Accounts Controller", "Provides customers with an summary of their bank accounts.", "Java REST Controller");
            Component securityComponent = restApi.addComponent("Security Component", "Provides functionality related to signing in, changing passwords, etc.", "Java Bean");
            Component mainframeBankingSystemFacade = restApi.addComponent("Mainframe Banking System Facade", "A facade onto the mainframe banking system.", "Java Bean");

            
            //TODO: Corregir
        
           
//            restApi.getComponents().where((c) -> "Net Core REST Controller".equals(c.Technology)).toList().forEach((c) -> webApplication.uses(c, "Uses", "HTTPS"));
            restApi.getComponents().stream().filter((c) -> "Java REST Controller".equals(c.getTechnology())).collect(Collectors.toList()).forEach((c) -> webApplication.uses(c, "Uses", "HTTPS"));

            signinController.uses(securityComponent, "Uses");
            accountsSummaryController.uses(mainframeBankingSystemFacade, "Uses");
            securityComponent.uses(database, "Reads from and writes to", "JDBC");
            mainframeBankingSystemFacade.uses(mainframeBankingSystem, "Uses", "XML/HTTPS");

            ComponentView componentViewForRestApi = viewSet.createComponentView(restApi, "Components", "The components diagram for the REST API");
            componentViewForRestApi.setPaperSize(PaperSize.A4_Landscape);
            componentViewForRestApi.addAllContainers();
            componentViewForRestApi.addAllComponents();
            componentViewForRestApi.add(cliente);
            componentViewForRestApi.add(mainframeBankingSystem);
            //componentViewForRestApi.EnableAutomaticLayout();
            structurizrClient.unlockWorkspace(workspaceId);
            structurizrClient.putWorkspace(workspaceId, workspace);
         
    }


}